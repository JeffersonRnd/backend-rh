package com.hotelreservas.mapper;

import com.hotelreservas.dto.HabitacionRequestDTO;
import com.hotelreservas.dto.HabitacionResponseDTO;
import com.hotelreservas.model.Habitacion;
import com.hotelreservas.model.TipoHabitacion;

public class HabitacionMapper {

    private HabitacionMapper() {}

    public static Habitacion toEntity(HabitacionRequestDTO dto) {
        Habitacion entity = new Habitacion();
        entity.setNumero(dto.getNumero());

        TipoHabitacion tipo = new TipoHabitacion();
        tipo.setId(dto.getTipoHabitacionId());
        entity.setTipoHabitacion(tipo);

        entity.setPrecioPorNoche(dto.getPrecioPorNoche());
        entity.setCapacidad(dto.getCapacidad());
        entity.setServicios(dto.getServicios());
        entity.setDisponible(dto.getDisponible() != null ? dto.getDisponible() : true);
        return entity;
    }

    public static HabitacionResponseDTO toResponseDTO(Habitacion entity) {
        return new HabitacionResponseDTO(
                entity.getId(),
                entity.getNumero(),
                entity.getTipoHabitacion() != null ? entity.getTipoHabitacion().getNombre() : null,
                entity.getPrecioPorNoche(),
                entity.getCapacidad(),
                entity.getServicios(),
                entity.getDisponible()
        );
    }
}
