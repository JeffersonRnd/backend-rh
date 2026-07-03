package com.hotelreservas.mapper;

import com.hotelreservas.dto.HuespedRequestDTO;
import com.hotelreservas.dto.HuespedResponseDTO;
import com.hotelreservas.model.Huesped;

public class HuespedMapper {

    private HuespedMapper() {}

    public static Huesped toEntity(HuespedRequestDTO dto) {
        Huesped entity = new Huesped();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDni(dto.getDni());
        entity.setCorreo(dto.getCorreo());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        return entity;
    }

    public static HuespedResponseDTO toResponseDTO(Huesped entity) {
        return new HuespedResponseDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getDni(),
                entity.getCorreo(),
                entity.getTelefono(),
                entity.getDireccion()
        );
    }
}
