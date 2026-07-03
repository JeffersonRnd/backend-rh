package com.hotelreservas.service.implementation;

import com.hotelreservas.exception.DuplicateResourceException;
import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.model.TipoHabitacion;
import com.hotelreservas.repository.ITipoHabitacionRepository;
import com.hotelreservas.service.ITipoHabitacionService;
import org.springframework.stereotype.Service;

@Service
public class TipoHabitacionService extends GenericServiceImpl<TipoHabitacion, Long> implements ITipoHabitacionService {

    private final ITipoHabitacionRepository tipoHabitacionRepository;

    public TipoHabitacionService(ITipoHabitacionRepository tipoHabitacionRepository) {
        super(tipoHabitacionRepository, "Tipo de habitación");
        this.tipoHabitacionRepository = tipoHabitacionRepository;
    }

    @Override
    public TipoHabitacion guardar(TipoHabitacion tipoHabitacion) {
        if (tipoHabitacionRepository.existsByNombre(tipoHabitacion.getNombre())) {
            throw new DuplicateResourceException("Ya existe un tipo de habitación con el nombre: " + tipoHabitacion.getNombre());
        }
        return super.guardar(tipoHabitacion);
    }

    @Override
    public TipoHabitacion actualizar(Long id, TipoHabitacion tipoActualizado) {
        TipoHabitacion existente = tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado con id: " + id));
        existente.setNombre(tipoActualizado.getNombre());
        existente.setDescripcion(tipoActualizado.getDescripcion());
        return tipoHabitacionRepository.save(existente);
    }
}
