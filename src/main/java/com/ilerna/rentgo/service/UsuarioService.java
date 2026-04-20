package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Usuario.
 * Contiene la logica de negocio entre el controlador y el repositorio.
 */
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    // Inyeccion de dependencias por constructor
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    /** Obtiene todos los usuarios. */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    /** Busca un usuario por su ID. */
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }
    /** Busca un usuario por su email. */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    /** Busca un usuario por su telefono. */
    public Optional<Usuario> buscarPorTelefono(String telefono) {
        return usuarioRepository.findByTelefono(telefono);
    }
    /** Busca un usuario por su DNI. */
    public Optional<Usuario> buscarPorDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }
    /** Guarda o actualiza un usuario. */
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    /** Elimina un usuario por su ID. */
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
    /** Cuenta los usuarios con tipo 'cliente'. */
    public long contarClientes() {
        return usuarioRepository.countByTipoUsuarioNombre("cliente");
    }
}
