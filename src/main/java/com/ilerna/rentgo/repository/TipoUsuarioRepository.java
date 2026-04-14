package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 * Repositorio JPA para la entidad TipoUsuario.
 * Al extender JpaRepository, Spring Data JPA nos da automaticamente
 * metodos CRUD: findAll(), findById(), save(), deleteById(), etc.
 */
@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Integer> {
    Optional<TipoUsuario> findByNombre(String nombre);
}
