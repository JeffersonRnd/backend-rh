package com.hotelreservas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGenericService<T, ID> {

    List<T> listarTodos();

    Page<T> listarPaginado(Pageable pageable);

    Optional<T> buscarPorId(ID id);

    T guardar(T entidad);

    T actualizar(ID id, T entidad);

    void eliminar(ID id);
}