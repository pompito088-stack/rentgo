package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 * Repositorio JPA para la entidad Vehiculo.
 */
@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    Optional<Vehiculo> findByMatricula(String matricula);
}

