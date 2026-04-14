package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
/**
 * Repositorio JPA para la entidad Reserva.
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuarioId(Integer usuarioId);

    /** Busca reservas solapadas para un vehiculo en un rango de fechas. */
    @Query("SELECT r FROM Reserva r WHERE r.vehiculo.id = :vehiculoId " +
           "AND r.estado <> 'cancelada' " +
           "AND r.fechaInicio <= :fechaFin AND r.fechaFin >= :fechaInicio")
    List<Reserva> findReservasSolapadas(@Param("vehiculoId") Integer vehiculoId,
                                        @Param("fechaInicio") LocalDate fechaInicio,
                                        @Param("fechaFin") LocalDate fechaFin);
}

