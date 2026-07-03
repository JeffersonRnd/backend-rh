package com.hotelreservas.controller;

import com.hotelreservas.dto.LoginRequestDTO;
import com.hotelreservas.dto.LoginResponseDTO;
import com.hotelreservas.dto.RegisterRequestDTO;
import com.hotelreservas.model.Usuario;
import com.hotelreservas.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    // Login: cualquier usuario (ADMIN o HUESPED) puede autenticarse
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Registro: crea un usuario con rol ADMIN o HUESPED
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }
}
