package com.hotelreservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repositorio genérico base.
 * Toda interfaz de repositorio específica puede extender de esta
 * (además de JpaRepository) para heredar operaciones CRUD comunes
 * y, si se desea, agregar métodos genéricos adicionales en el futuro.
 *
 * @param <T>  Tipo de la entidad
 * @param <ID> Tipo del identificador de la entidad
 */
@NoRepositoryBean
public interface IGenericRepository<T, ID> extends JpaRepository<T, ID> {
}
