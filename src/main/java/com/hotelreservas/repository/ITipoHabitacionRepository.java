package com.hotelreservas.repository;

import com.hotelreservas.model.TipoHabitacion;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoHabitacionRepository extends IGenericRepository<TipoHabitacion, Long> {
    boolean existsByNombre(String nombre);
}