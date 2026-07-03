package com.hotelreservas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleReservaRequestDTO {

    @NotNull(message = "El id de la habitación es obligatorio")
    private Long habitacionId;

    @NotNull(message = "La cantidad de noches es obligatoria")
    @Positive(message = "La cantidad de noches debe ser mayor a 0")
    private Integer cantidadNoches;
}
