package com.hotelreservas.controller;

import com.hotelreservas.dto.TipoHabitacionRequestDTO;
import com.hotelreservas.dto.TipoHabitacionResponseDTO;
import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.mapper.TipoHabitacionMapper;
import com.hotelreservas.model.TipoHabitacion;
import com.hotelreservas.service.ITipoHabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipos-habitacion")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TipoHabitacionController {

    private final ITipoHabitacionService tipoHabitacionService;

    @GetMapping
    public ResponseEntity<List<TipoHabitacionResponseDTO>> listarTodos() {
        List<TipoHabitacionResponseDTO> tipos = tipoHabitacionService.listarTodos().stream()
                .map(TipoHabitacionMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoHabitacionResponseDTO> buscarPorId(@PathVariable Long id) {
        TipoHabitacion tipo = tipoHabitacionService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado con id: " + id));
        return ResponseEntity.ok(TipoHabitacionMapper.toResponseDTO(tipo));
    }

    @PostMapping
    public ResponseEntity<TipoHabitacionResponseDTO> crear(@Valid @RequestBody TipoHabitacionRequestDTO dto) {
        TipoHabitacion creado = tipoHabitacionService.guardar(TipoHabitacionMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(TipoHabitacionMapper.toResponseDTO(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoHabitacionResponseDTO> actualizar(@PathVariable Long id,
                                                                  @Valid @RequestBody TipoHabitacionRequestDTO dto) {
        TipoHabitacion actualizado = tipoHabitacionService.actualizar(id, TipoHabitacionMapper.toEntity(dto));
        return ResponseEntity.ok(TipoHabitacionMapper.toResponseDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoHabitacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
