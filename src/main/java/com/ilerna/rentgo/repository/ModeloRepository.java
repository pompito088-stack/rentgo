package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Repositorio JPA para la entidad Modelo.
 */
@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Integer> {
    List<Modelo> findByMarcaId(Integer marcaId);
}

