package com.hotelreservas.service;

import com.hotelreservas.model.DetalleReserva;

import java.util.List;

public interface IDetalleReservaService extends IGenericService<DetalleReserva, Long> {
    List<DetalleReserva> listarPorReserva(Long reservaId);
}
