package com.ilerna.rentgo.repository;

import com.ilerna.rentgo.model.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Devolucion.
 */
@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Integer> {

    /** Busca la devolución asociada a un pago concreto. */
    Optional<Devolucion> findByPagoId(Integer pagoId);

    /** Lista las devoluciones de los pagos de un usuario (a través de sus reservas). */
    List<Devolucion> findByPagoReservaUsuarioId(Integer usuarioId);
}

