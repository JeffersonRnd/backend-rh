package com.hotelreservas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleReservaResponseDTO {
    private Long id;
    private String habitacionNumero;
    private String habitacionTipo;
    private Integer cantidadNoches;
    private Double precioUnitario;
    private Double subtotal;
}
