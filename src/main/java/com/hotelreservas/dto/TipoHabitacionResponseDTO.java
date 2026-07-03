package com.hotelreservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoHabitacionResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}