package com.hotelreservas.controller;

import com.hotelreservas.dto.HabitacionRequestDTO;
import com.hotelreservas.dto.HabitacionResponseDTO;
import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.mapper.HabitacionMapper;
import com.hotelreservas.model.Habitacion;
import com.hotelreservas.service.IHabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HabitacionController {

    private final IHabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<Page<HabitacionResponseDTO>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HabitacionResponseDTO> habitaciones = habitacionService.listarPaginado(pageable)
                .map(HabitacionMapper::toResponseDTO);
        return ResponseEntity.ok(habitaciones);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<HabitacionResponseDTO>> listarDisponibles() {
        List<HabitacionResponseDTO> habitaciones = habitacionService.listarDisponibles().stream()
                .map(HabitacionMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(habitaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> buscarPorId(@PathVariable Long id) {
        Habitacion habitacion = habitacionService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + id));
        return ResponseEntity.ok(HabitacionMapper.toResponseDTO(habitacion));
    }

    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> crear(@Valid @RequestBody HabitacionRequestDTO dto) {
        Habitacion creada = habitacionService.guardar(HabitacionMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(HabitacionMapper.toResponseDTO(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody HabitacionRequestDTO dto) {
        Habitacion actualizada = habitacionService.actualizar(id, HabitacionMapper.toEntity(dto));
        return ResponseEntity.ok(HabitacionMapper.toResponseDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        habitacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}