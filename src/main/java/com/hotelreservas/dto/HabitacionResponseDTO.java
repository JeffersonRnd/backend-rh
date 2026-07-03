package com.hotelreservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionResponseDTO {
    private Long id;
    private String numero;
    private String tipoHabitacionNombre;   // nombre plano, no el objeto anidado
    private Double precioPorNoche;
    private Integer capacidad;
    private String servicios;
    private Boolean disponible;
}