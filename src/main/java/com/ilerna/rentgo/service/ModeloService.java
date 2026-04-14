package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Modelo;
import com.ilerna.rentgo.repository.ModeloRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Modelo.
 */
@Service
public class ModeloService {
    private final ModeloRepository modeloRepository;

    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }
    /** Obtiene todos los modelos. */
    public List<Modelo> listarTodos() {
        return modeloRepository.findAll();
    }
    /** Busca un modelo por su ID. */
    public Optional<Modelo> buscarPorId(Integer id) {
        return modeloRepository.findById(id);
    }
    /** Obtiene modelos de una marca concreta. */
    public List<Modelo> listarPorMarca(Integer marcaId) {
        return modeloRepository.findByMarcaId(marcaId);
    }
    /** Guarda o actualiza un modelo. */
    public Modelo guardar(Modelo modelo) {
        return modeloRepository.save(modelo);
    }
    /** Elimina un modelo por su ID. */
    public void eliminar(Integer id) {
        modeloRepository.deleteById(id);
    }
}

