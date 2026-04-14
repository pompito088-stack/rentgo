package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Repositorio JPA para la entidad Mantenimiento.
 */
@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {
    List<Mantenimiento> findByVehiculoId(Integer vehiculoId);
}

