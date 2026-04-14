package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.TipoUsuario;
import com.ilerna.rentgo.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad TipoUsuario.
 * Contiene la logica de negocio entre el controlador y el repositorio.
 */
@Service
public class TipoUsuarioService {
    private final TipoUsuarioRepository tipoUsuarioRepository;
    // Inyeccion de dependencias por constructor
    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }
    /** Obtiene todos los tipos de usuario. */
    public List<TipoUsuario> listarTodos() {
        return tipoUsuarioRepository.findAll();
    }
    /** Busca un tipo de usuario por su ID. */
    public Optional<TipoUsuario> buscarPorId(Integer id) {
        return tipoUsuarioRepository.findById(id);
    }
    /** Busca un tipo de usuario por su nombre. */
    public Optional<TipoUsuario> buscarPorNombre(String nombre) {
        return tipoUsuarioRepository.findByNombre(nombre);
    }
    /** Guarda o actualiza un tipo de usuario. */
    public TipoUsuario guardar(TipoUsuario tipoUsuario) {
        return tipoUsuarioRepository.save(tipoUsuario);
    }
    /** Elimina un tipo de usuario por su ID. */
    public void eliminar(Integer id) {
        tipoUsuarioRepository.deleteById(id);
    }
}
