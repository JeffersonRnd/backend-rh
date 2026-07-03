package com.hotelreservas.service;

import com.hotelreservas.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IReservaService extends IGenericService<Reserva, Long> {
    List<Reserva> listarPorHuesped(Long huespedId);
    Page<Reserva> listarPorHuespedPaginado(Long huespedId, Pageable pageable);
    List<Reserva> listarPorEstado(String estado);
}