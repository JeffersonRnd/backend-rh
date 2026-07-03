package com.hotelreservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionRequestDTO {

    @NotBlank(message = "El número de habitación es obligatorio")
    private String numero;

    @NotNull(message = "El tipo de habitación es obligatorio")
    private Long tipoHabitacionId;   // solo el ID, no el objeto completo

    @NotNull(message = "El precio por noche es obligatorio")
    @Positive(message = "El precio por noche debe ser mayor a 0")
    private Double precioPorNoche;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;

    private String servicios;
    private Boolean disponible;
}
