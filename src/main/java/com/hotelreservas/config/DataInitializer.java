package com.hotelreservas.config;

import com.hotelreservas.model.Rol;
import com.hotelreservas.model.Usuario;
import com.hotelreservas.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea un usuario ADMIN por defecto al iniciar la aplicación,
 * si todavía no existe ningún usuario con ese username.
 *
 * Credenciales por defecto (cambiar en producción):
 *   username: admin
 *   password: admin123
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            admin.setHabilitado(true);
            usuarioRepository.save(admin);
        }
    }
}
