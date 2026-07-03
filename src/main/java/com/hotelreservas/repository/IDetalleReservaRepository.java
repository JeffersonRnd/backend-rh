package com.hotelreservas.repository;

import com.hotelreservas.model.DetalleReserva;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDetalleReservaRepository extends IGenericRepository<DetalleReserva, Long> {
    List<DetalleReserva> findByReservaId(Long reservaId);
}