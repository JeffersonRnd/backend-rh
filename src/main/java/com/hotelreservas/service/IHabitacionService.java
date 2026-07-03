package com.hotelreservas.service;

import com.hotelreservas.model.Habitacion;

import java.util.List;

public interface IHabitacionService extends IGenericService<Habitacion, Long> {
    List<Habitacion> listarDisponibles();
}
