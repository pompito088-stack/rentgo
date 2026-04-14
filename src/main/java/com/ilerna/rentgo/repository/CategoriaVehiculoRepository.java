package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.CategoriaVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 * Repositorio JPA para la entidad CategoriaVehiculo.
 */
@Repository
public interface CategoriaVehiculoRepository extends JpaRepository<CategoriaVehiculo, Integer> {
    Optional<CategoriaVehiculo> findByNombre(String nombre);
}

