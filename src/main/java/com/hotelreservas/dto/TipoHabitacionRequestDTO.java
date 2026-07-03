package com.hotelreservas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoHabitacionRequestDTO {

    @NotBlank(message = "El nombre del tipo de habitación es obligatorio")
    private String nombre;

    private String descripcion;
}
