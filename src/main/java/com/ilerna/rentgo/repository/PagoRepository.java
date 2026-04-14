package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 * Repositorio JPA para la entidad Pago.
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    Optional<Pago> findByReservaId(Integer reservaId);
}

