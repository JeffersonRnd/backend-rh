package com.hotelreservas.mapper;

import com.hotelreservas.dto.DetalleReservaRequestDTO;
import com.hotelreservas.dto.DetalleReservaResponseDTO;
import com.hotelreservas.dto.ReservaRequestDTO;
import com.hotelreservas.dto.ReservaResponseDTO;
import com.hotelreservas.model.DetalleReserva;
import com.hotelreservas.model.Habitacion;
import com.hotelreservas.model.Huesped;
import com.hotelreservas.model.Reserva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReservaMapper {

    private ReservaMapper() {}

    public static Reserva toEntity(ReservaRequestDTO dto) {
        Reserva entity = new Reserva();

        Huesped huesped = new Huesped();
        huesped.setId(dto.getHuespedId());
        entity.setHuesped(huesped);

        entity.setFechaIngreso(dto.getFechaIngreso());
        entity.setFechaSalida(dto.getFechaSalida());
        entity.setEstado(dto.getEstado());
        entity.setObservaciones(dto.getObservaciones());

        List<DetalleReserva> detalles = new ArrayList<>();
        if (dto.getDetalles() != null) {
            for (DetalleReservaRequestDTO detalleDTO : dto.getDetalles()) {
                DetalleReserva detalle = new DetalleReserva();

                Habitacion habitacion = new Habitacion();
                habitacion.setId(detalleDTO.getHabitacionId());
                detalle.setHabitacion(habitacion);

                detalle.setCantidadNoches(detalleDTO.getCantidadNoches());
                detalles.add(detalle);
            }
        }
        entity.setDetalles(detalles);

        return entity;
    }

    public static ReservaResponseDTO toResponseDTO(Reserva entity) {
        List<DetalleReservaResponseDTO> detalles = entity.getDetalles() == null
                ? Collections.emptyList()
                : entity.getDetalles().stream()
                        .map(ReservaMapper::detalleToResponseDTO)
                        .collect(Collectors.toList());

        return new ReservaResponseDTO(
                entity.getId(),
                entity.getHuesped() != null
                        ? entity.getHuesped().getNombre() + " " + entity.getHuesped().getApellido()
                        : null,
                entity.getHuesped() != null ? entity.getHuesped().getDni() : null,
                entity.getFechaIngreso(),
                entity.getFechaSalida(),
                entity.getTotalPagar(),
                entity.getEstado(),
                entity.getObservaciones(),
                detalles
        );
    }

    public static DetalleReservaResponseDTO detalleToResponseDTO(DetalleReserva detalle) {
        return new DetalleReservaResponseDTO(
                detalle.getId(),
                detalle.getHabitacion() != null ? detalle.getHabitacion().getNumero() : null,
                detalle.getHabitacion() != null && detalle.getHabitacion().getTipoHabitacion() != null
                        ? detalle.getHabitacion().getTipoHabitacion().getNombre()
                        : null,
                detalle.getCantidadNoches(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
