package com.hotelreservas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para registrar un nuevo usuario.
 * Si el rol es HUESPED, los datos personales (nombre, apellido, dni, correo)
 * se usan para crear también el registro asociado en la tabla Huesped.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El rol es obligatorio (ADMIN o HUESPED)")
    private String rol;

    // Datos opcionales para crear el Huesped asociado (solo si rol = HUESPED)
    private String nombre;
    private String apellido;
    private String dni;

    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    private String telefono;
    private String direccion;
}
