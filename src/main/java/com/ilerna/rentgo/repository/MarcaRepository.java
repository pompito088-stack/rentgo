package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 * Repositorio JPA para la entidad Marca.
 */
@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    Optional<Marca> findByNombre(String nombre);
}

