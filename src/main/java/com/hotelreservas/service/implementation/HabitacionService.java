package com.hotelreservas.service.implementation;

import com.hotelreservas.exception.DuplicateResourceException;
import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.model.Habitacion;
import com.hotelreservas.model.TipoHabitacion;
import com.hotelreservas.repository.IHabitacionRepository;
import com.hotelreservas.repository.ITipoHabitacionRepository;
import com.hotelreservas.service.IHabitacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitacionService extends GenericServiceImpl<Habitacion, Long> implements IHabitacionService {

    private final IHabitacionRepository habitacionRepository;
    private final ITipoHabitacionRepository tipoHabitacionRepository;

    public HabitacionService(IHabitacionRepository habitacionRepository,
                              ITipoHabitacionRepository tipoHabitacionRepository) {
        super(habitacionRepository, "Habitación");
        this.habitacionRepository = habitacionRepository;
        this.tipoHabitacionRepository = tipoHabitacionRepository;
    }

    @Override
    public List<Habitacion> listarDisponibles() {
        return habitacionRepository.findByDisponibleTrue();
    }

    @Override
    public Habitacion guardar(Habitacion habitacion) {
        if (habitacionRepository.existsByNumero(habitacion.getNumero())) {
            throw new DuplicateResourceException("Ya existe una habitación con el número: " + habitacion.getNumero());
        }
        TipoHabitacion tipo = tipoHabitacionRepository.findById(habitacion.getTipoHabitacion().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado"));
        habitacion.setTipoHabitacion(tipo);
        return super.guardar(habitacion);
    }

    @Override
    public Habitacion actualizar(Long id, Habitacion habitacionActualizada) {
        Habitacion existente = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + id));
        TipoHabitacion tipo = tipoHabitacionRepository.findById(habitacionActualizada.getTipoHabitacion().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado"));
        existente.setNumero(habitacionActualizada.getNumero());
        existente.setTipoHabitacion(tipo);
        existente.setPrecioPorNoche(habitacionActualizada.getPrecioPorNoche());
        existente.setCapacidad(habitacionActualizada.getCapacidad());
        existente.setServicios(habitacionActualizada.getServicios());
        existente.setDisponible(habitacionActualizada.getDisponible());
        return habitacionRepository.save(existente);
    }
}
