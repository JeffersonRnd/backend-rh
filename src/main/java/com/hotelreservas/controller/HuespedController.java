package com.hotelreservas.controller;

import com.hotelreservas.dto.HuespedRequestDTO;
import com.hotelreservas.dto.HuespedResponseDTO;
import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.mapper.HuespedMapper;
import com.hotelreservas.model.Huesped;
import com.hotelreservas.model.Usuario;
import com.hotelreservas.service.IHuespedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HuespedController {

    private final IHuespedService huespedService;

    @GetMapping
    public ResponseEntity<Page<HuespedResponseDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HuespedResponseDTO> huespedes = huespedService.listarPaginado(pageable)
                .map(HuespedMapper::toResponseDTO);
        return ResponseEntity.ok(huespedes);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'HUESPED')")
    public ResponseEntity<HuespedResponseDTO> obtenerPropio() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        if (usuario.getHuesped() == null) {
            throw new ResourceNotFoundException("El usuario autenticado no tiene un huésped asociado");
        }

        return ResponseEntity.ok(HuespedMapper.toResponseDTO(usuario.getHuesped()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuespedResponseDTO> buscarPorId(@PathVariable Long id) {
        Huesped huesped = huespedService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Huésped no encontrado con id: " + id));
        return ResponseEntity.ok(HuespedMapper.toResponseDTO(huesped));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<HuespedResponseDTO> buscarPorDni(@PathVariable String dni) {
        Huesped huesped = huespedService.buscarPorDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Huésped no encontrado con DNI: " + dni));
        return ResponseEntity.ok(HuespedMapper.toResponseDTO(huesped));
    }

    @PostMapping
    public ResponseEntity<HuespedResponseDTO> crear(@Valid @RequestBody HuespedRequestDTO dto) {
        Huesped creado = huespedService.guardar(HuespedMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(HuespedMapper.toResponseDTO(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HuespedResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody HuespedRequestDTO dto) {
        Huesped actualizado = huespedService.actualizar(id, HuespedMapper.toEntity(dto));
        return ResponseEntity.ok(HuespedMapper.toResponseDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        huespedService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}