package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Marca;
import com.ilerna.rentgo.repository.MarcaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Marca.
 */
@Service
public class MarcaService {
    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }
    /** Obtiene todas las marcas. */
    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }
    /** Busca una marca por su ID. */
    public Optional<Marca> buscarPorId(Integer id) {
        return marcaRepository.findById(id);
    }
    /** Busca una marca por su nombre. */
    public Optional<Marca> buscarPorNombre(String nombre) {
        return marcaRepository.findByNombre(nombre);
    }
    /** Guarda o actualiza una marca. */
    public Marca guardar(Marca marca) {
        return marcaRepository.save(marca);
    }
    /** Elimina una marca por su ID. */
    public void eliminar(Integer id) {
        marcaRepository.deleteById(id);
    }
}

