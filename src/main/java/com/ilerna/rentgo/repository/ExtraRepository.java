package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Extra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio JPA para la entidad Extra.
 */
@Repository
public interface ExtraRepository extends JpaRepository<Extra, Integer> {
}

