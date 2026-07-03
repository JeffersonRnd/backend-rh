package com.hotelreservas.controller;

import com.hotelreservas.dto.ReservaRequestDTO;
import com.hotelreservas.dto.ReservaResponseDTO;
import com.hotelreservas.exception.BadRequestException;
import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.mapper.ReservaMapper;
import com.hotelreservas.model.Reserva;
import com.hotelreservas.model.Usuario;
import com.hotelreservas.service.IReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReservaController {

    private final IReservaService reservaService;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ReservaResponseDTO>>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Usuario usuario = usuarioActual();
        Pageable pageable = PageRequest.of(page, size);

        Page<Reserva> reservas = esAdmin(usuario)
                ? reservaService.listarPaginado(pageable)
                : reservaService.listarPorHuespedPaginado(usuario.getHuesped().getId(), pageable);

        List<EntityModel<ReservaResponseDTO>> modelos = reservas.getContent().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                reservas.getSize(), reservas.getNumber(), reservas.getTotalElements(), reservas.getTotalPages());

        return ResponseEntity.ok(PagedModel.of(modelos, metadata));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> buscarPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        verificarAccesoOLanzar(reserva);

        return ResponseEntity.ok(toModel(reserva));
    }

    @GetMapping("/huesped/{huespedId}")
    public ResponseEntity<CollectionModel<EntityModel<ReservaResponseDTO>>> listarPorHuesped(
            @PathVariable Long huespedId) {

        Usuario usuario = usuarioActual();
        if (!esAdmin(usuario) && !huespedId.equals(usuario.getHuesped().getId())) {
            throw new BadRequestException("No puede consultar reservas de otro huésped");
        }

        List<EntityModel<ReservaResponseDTO>> modelos = reservaService.listarPorHuesped(huespedId).stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(ReservaController.class).listarPorHuesped(huespedId)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(modelos, selfLink));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<ReservaResponseDTO>>> listarPorEstado(
            @PathVariable String estado) {

        List<EntityModel<ReservaResponseDTO>> modelos = reservaService.listarPorEstado(estado.toUpperCase()).stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(ReservaController.class).listarPorEstado(estado)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(modelos, selfLink));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ReservaResponseDTO>> crear(@Valid @RequestBody ReservaRequestDTO dto) {
        Usuario usuario = usuarioActual();

        if (!esAdmin(usuario)) {
            if (usuario.getHuesped() == null || !usuario.getHuesped().getId().equals(dto.getHuespedId())) {
                throw new BadRequestException("Solo puede crear reservas para su propio usuario");
            }
        }

        Reserva creada = reservaService.guardar(ReservaMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> actualizar(@PathVariable Long id,
                                                                      @Valid @RequestBody ReservaRequestDTO dto) {
        Reserva existente = reservaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
        verificarAccesoOLanzar(existente);

        Reserva actualizada = reservaService.actualizar(id, ReservaMapper.toEntity(dto));
        return ResponseEntity.ok(toModel(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Reserva existente = reservaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
        verificarAccesoOLanzar(existente);

        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<ReservaResponseDTO> toModel(Reserva reserva) {
        ReservaResponseDTO dto = ReservaMapper.toResponseDTO(reserva);

        EntityModel<ReservaResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(ReservaController.class).buscarPorId(reserva.getId())).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel("reservas"),
                linkTo(methodOn(DetalleReservaController.class).listarPorReserva(reserva.getId())).withRel("detalles")
        );

        if (reserva.getHuesped() != null) {
            model.add(linkTo(methodOn(HuespedController.class)
                    .buscarPorId(reserva.getHuesped().getId())).withRel("huesped"));
        }

        return model;
    }

    private Usuario usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }

    private boolean esAdmin(Usuario usuario) {
        return usuario.getRol().name().equals("ADMIN");
    }

    private void verificarAccesoOLanzar(Reserva reserva) {
        Usuario usuario = usuarioActual();
        if (esAdmin(usuario)) {
            return;
        }
        if (usuario.getHuesped() == null
                || reserva.getHuesped() == null
                || !reserva.getHuesped().getId().equals(usuario.getHuesped().getId())) {
            throw new BadRequestException("No tiene acceso a esta reserva");
        }
    }
}