package com.hotelreservas.service.implementation;

import com.hotelreservas.exception.ResourceNotFoundException;
import com.hotelreservas.repository.IGenericRepository;
import com.hotelreservas.service.IGenericService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class GenericServiceImpl<T, ID> implements IGenericService<T, ID> {

    protected final IGenericRepository<T, ID> repository;
    private final String nombreEntidad;

    protected GenericServiceImpl(IGenericRepository<T, ID> repository, String nombreEntidad) {
        this.repository = repository;
        this.nombreEntidad = nombreEntidad;
    }

    @Override
    public List<T> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Page<T> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<T> buscarPorId(ID id) {
        return repository.findById(id);
    }

    @Override
    public T guardar(T entidad) {
        return repository.save(entidad);
    }

    @Override
    public T actualizar(ID id, T entidad) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(nombreEntidad + " no encontrado con id: " + id);
        }
        return repository.save(entidad);
    }

    @Override
    public void eliminar(ID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(nombreEntidad + " no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}