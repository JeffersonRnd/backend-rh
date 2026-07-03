package com.hotelreservas.service.implementation;

import com.hotelreservas.exception.DuplicateResourceException;
import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.model.Huesped;
import com.hotelreservas.repository.IHuespedRepository;
import com.hotelreservas.service.IHuespedService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HuespedService extends GenericServiceImpl<Huesped, Long> implements IHuespedService {

    private final IHuespedRepository huespedRepository;

    public HuespedService(IHuespedRepository huespedRepository) {
        super(huespedRepository, "Huésped");
        this.huespedRepository = huespedRepository;
    }

    @Override
    public Optional<Huesped> buscarPorDni(String dni) {
        return huespedRepository.findByDni(dni);
    }

    @Override
    public Huesped guardar(Huesped huesped) {
        if (huespedRepository.existsByDni(huesped.getDni())) {
            throw new DuplicateResourceException("Ya existe un huésped con el DNI: " + huesped.getDni());
        }
        if (huespedRepository.existsByCorreo(huesped.getCorreo())) {
            throw new DuplicateResourceException("Ya existe un huésped con el correo: " + huesped.getCorreo());
        }
        return super.guardar(huesped);
    }

    @Override
    public Huesped actualizar(Long id, Huesped huespedActualizado) {
        Huesped existente = huespedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Huésped no encontrado con id: " + id));
        existente.setNombre(huespedActualizado.getNombre());
        existente.setApellido(huespedActualizado.getApellido());
        existente.setDni(huespedActualizado.getDni());
        existente.setCorreo(huespedActualizado.getCorreo());
        existente.setTelefono(huespedActualizado.getTelefono());
        existente.setDireccion(huespedActualizado.getDireccion());
        return huespedRepository.save(existente);
    }
}
