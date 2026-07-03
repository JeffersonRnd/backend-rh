package com.hotelreservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuespedResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String correo;
    private String telefono;
    private String direccion;
}
