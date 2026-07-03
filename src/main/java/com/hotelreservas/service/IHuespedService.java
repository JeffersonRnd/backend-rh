package com.hotelreservas.service;

import com.hotelreservas.model.Huesped;

import java.util.Optional;

public interface IHuespedService extends IGenericService<Huesped, Long> {
    Optional<Huesped> buscarPorDni(String dni);
}
