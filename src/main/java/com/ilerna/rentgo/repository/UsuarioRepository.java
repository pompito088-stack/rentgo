package com.ilerna.rentgo.repository;
import com.ilerna.rentgo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 * Repositorio JPA para la entidad Usuario.
 * Hereda todos los metodos CRUD de JpaRepository.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTelefono(String telefono);
    Optional<Usuario> findByDni(String dni);
}
