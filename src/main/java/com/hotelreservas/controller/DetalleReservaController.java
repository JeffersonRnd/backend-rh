package com.hotelreservas.controller;

import com.hotelreservas.dto.DetalleReservaResponseDTO;
import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.mapper.ReservaMapper;
import com.hotelreservas.model.DetalleReserva;
import com.hotelreservas.service.IDetalleReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/detalles-reserva")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DetalleReservaController {

    private final IDetalleReservaService detalleReservaService;

    @GetMapping
    public ResponseEntity<List<DetalleReservaResponseDTO>> listarTodos() {
        List<DetalleReservaResponseDTO> detalles = detalleReservaService.listarTodos().stream()
                .map(ReservaMapper::detalleToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleReservaResponseDTO> buscarPorId(@PathVariable Long id) {
        DetalleReserva detalle = detalleReservaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de reserva no encontrado con id: " + id));
        return ResponseEntity.ok(ReservaMapper.detalleToResponseDTO(detalle));
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<DetalleReservaResponseDTO>> listarPorReserva(@PathVariable Long reservaId) {
        List<DetalleReservaResponseDTO> detalles = detalleReservaService.listarPorReserva(reservaId).stream()
                .map(ReservaMapper::detalleToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(detalles);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        detalleReservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
