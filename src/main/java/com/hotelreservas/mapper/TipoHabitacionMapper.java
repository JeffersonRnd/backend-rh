package com.hotelreservas.mapper;

import com.hotelreservas.dto.TipoHabitacionRequestDTO;
import com.hotelreservas.dto.TipoHabitacionResponseDTO;
import com.hotelreservas.model.TipoHabitacion;

public class TipoHabitacionMapper {

    private TipoHabitacionMapper() {}

    public static TipoHabitacion toEntity(TipoHabitacionRequestDTO dto) {
        TipoHabitacion entity = new TipoHabitacion();
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }

    public static TipoHabitacionResponseDTO toResponseDTO(TipoHabitacion entity) {
        return new TipoHabitacionResponseDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion()
        );
    }
}
