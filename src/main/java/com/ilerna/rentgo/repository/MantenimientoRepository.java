package com.ilerna.rentgo.repository;

import com.ilerna.rentgo.model.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la entidad Mantenimiento.
 */
@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {
    List<Mantenimiento> findByVehiculoId(Integer vehiculoId);

    /** Busca mantenimientos solapados para un vehiculo en un rango de fechas. */
    @Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.id = :vehiculoId " +
           "AND m.fechaInicio <= :fechaFin AND m.fechaFin >= :fechaInicio")
    List<Mantenimiento> findMantenimientosSolapados(@Param("vehiculoId") Integer vehiculoId,
                                                    @Param("fechaInicio") LocalDate fechaInicio,
                                                    @Param("fechaFin") LocalDate fechaFin);
}
