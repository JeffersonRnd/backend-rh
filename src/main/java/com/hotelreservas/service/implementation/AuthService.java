package com.hotelreservas.service.implementation;

import com.hotelreservas.dto.LoginRequestDTO;
import com.hotelreservas.dto.LoginResponseDTO;
import com.hotelreservas.dto.RegisterRequestDTO;
import com.hotelreservas.exception.BadRequestException;
import com.hotelreservas.exception.DuplicateResourceException;
import com.hotelreservas.model.Huesped;
import com.hotelreservas.model.Rol;
import com.hotelreservas.model.Usuario;
import com.hotelreservas.repository.IHuespedRepository;
import com.hotelreservas.repository.IUsuarioRepository;
import com.hotelreservas.security.jwt.JwtUtils;
import com.hotelreservas.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUsuarioRepository usuarioRepository;
    private final IHuespedRepository huespedRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtils.generateToken(authentication);
        Usuario usuario = (Usuario) authentication.getPrincipal();

        return new LoginResponseDTO(token, usuario.getUsername(), usuario.getRol().name());
    }

    @Override
    @Transactional
    public Usuario register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Ya existe un usuario con el username: " + request.getUsername());
        }

        Rol rol;
        try {
            rol = Rol.valueOf(request.getRol().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Rol inválido. Valores permitidos: ADMIN, HUESPED");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);
        usuario.setHabilitado(true);

        // Si el rol es HUESPED, se crea también el registro de Huesped asociado
        if (rol == Rol.HUESPED) {
            if (request.getDni() == null || request.getDni().isBlank()
                    || request.getCorreo() == null || request.getCorreo().isBlank()
                    || request.getNombre() == null || request.getNombre().isBlank()
                    || request.getApellido() == null || request.getApellido().isBlank()) {
                throw new BadRequestException(
                        "Para registrarse como HUESPED debe indicar nombre, apellido, dni y correo");
            }

            if (huespedRepository.existsByDni(request.getDni())) {
                throw new DuplicateResourceException("Ya existe un huésped con el DNI: " + request.getDni());
            }
            if (huespedRepository.existsByCorreo(request.getCorreo())) {
                throw new DuplicateResourceException("Ya existe un huésped con el correo: " + request.getCorreo());
            }

            Huesped huesped = new Huesped();
            huesped.setNombre(request.getNombre());
            huesped.setApellido(request.getApellido());
            huesped.setDni(request.getDni());
            huesped.setCorreo(request.getCorreo());
            huesped.setTelefono(request.getTelefono());
            huesped.setDireccion(request.getDireccion());

            huesped = huespedRepository.save(huesped);
            usuario.setHuesped(huesped);
        }

        return usuarioRepository.save(usuario);
    }
}
