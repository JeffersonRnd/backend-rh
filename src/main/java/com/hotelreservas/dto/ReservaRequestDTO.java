package com.hotelreservas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    @NotNull(message = "El id del huésped es obligatorio")
    private Long huespedId;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDate fechaSalida;

    private String estado;
    private String observaciones;

    @NotEmpty(message = "La reserva debe tener al menos un detalle")
    @Valid
    private List<DetalleReservaRequestDTO> detalles;
}
