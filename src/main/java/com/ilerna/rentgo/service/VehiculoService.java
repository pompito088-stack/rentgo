package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Vehiculo;
import com.ilerna.rentgo.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Vehiculo.
 */
@Service
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }
    /** Obtiene todos los vehiculos. */
    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }
    /** Busca un vehiculo por su ID. */
    public Optional<Vehiculo> buscarPorId(Integer id) {
        return vehiculoRepository.findById(id);
    }
    /** Busca un vehiculo por su matricula. */
    public Optional<Vehiculo> buscarPorMatricula(String matricula) {
        return vehiculoRepository.findByMatricula(matricula);
    }
    /** Guarda o actualiza un vehiculo. */
    public Vehiculo guardar(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }
    /** Elimina un vehiculo por su ID. */
    public void eliminar(Integer id) {
        vehiculoRepository.deleteById(id);
    }
}

