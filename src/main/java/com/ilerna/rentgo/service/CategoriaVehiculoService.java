package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.CategoriaVehiculo;
import com.ilerna.rentgo.repository.CategoriaVehiculoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad CategoriaVehiculo.
 */
@Service
public class CategoriaVehiculoService {
    private final CategoriaVehiculoRepository categoriaVehiculoRepository;

    public CategoriaVehiculoService(CategoriaVehiculoRepository categoriaVehiculoRepository) {
        this.categoriaVehiculoRepository = categoriaVehiculoRepository;
    }
    /** Obtiene todas las categorias. */
    public List<CategoriaVehiculo> listarTodas() {
        return categoriaVehiculoRepository.findAll();
    }
    /** Busca una categoria por su ID. */
    public Optional<CategoriaVehiculo> buscarPorId(Integer id) {
        return categoriaVehiculoRepository.findById(id);
    }
    /** Guarda o actualiza una categoria. */
    public CategoriaVehiculo guardar(CategoriaVehiculo categoria) {
        return categoriaVehiculoRepository.save(categoria);
    }
    /** Elimina una categoria por su ID. */
    public void eliminar(Integer id) {
        categoriaVehiculoRepository.deleteById(id);
    }
}

