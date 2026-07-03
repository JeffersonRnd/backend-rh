package com.hotelreservas.repository;

import com.hotelreservas.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservaRepository extends IGenericRepository<Reserva, Long> {
    List<Reserva> findByHuespedId(Long huespedId);
    Page<Reserva> findByHuespedId(Long huespedId, Pageable pageable);
    List<Reserva> findByEstado(String estado);
}