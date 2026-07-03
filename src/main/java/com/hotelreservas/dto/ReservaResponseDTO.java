package com.hotelreservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {
    private Long id;
    private String huespedNombre;      // nombre + apellido plano
    private String huespedDni;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private Double totalPagar;
    private String estado;
    private String observaciones;
    private List<DetalleReservaResponseDTO> detalles;
}
