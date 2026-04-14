package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio JPA para la entidad Sucursal.
 */
@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
}

