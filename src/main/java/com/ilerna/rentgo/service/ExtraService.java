package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Extra;
import com.ilerna.rentgo.repository.ExtraRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Extra.
 */
@Service
public class ExtraService {
    private final ExtraRepository extraRepository;

    public ExtraService(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }
    /** Obtiene todos los extras. */
    public List<Extra> listarTodos() {
        return extraRepository.findAll();
    }
    /** Busca un extra por su ID. */
    public Optional<Extra> buscarPorId(Integer id) {
        return extraRepository.findById(id);
    }
    /** Guarda o actualiza un extra. */
    public Extra guardar(Extra extra) {
        return extraRepository.save(extra);
    }
    /** Elimina un extra por su ID. */
    public void eliminar(Integer id) {
        extraRepository.deleteById(id);
    }
}

