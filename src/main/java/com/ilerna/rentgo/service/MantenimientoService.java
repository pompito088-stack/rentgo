package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Mantenimiento;
import com.ilerna.rentgo.repository.MantenimientoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Mantenimiento.
 */
@Service
public class MantenimientoService {
    private final MantenimientoRepository mantenimientoRepository;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
    }
    /** Obtiene todos los mantenimientos. */
    public List<Mantenimiento> listarTodos() {
        return mantenimientoRepository.findAll();
    }
    /** Busca un mantenimiento por su ID. */
    public Optional<Mantenimiento> buscarPorId(Integer id) {
        return mantenimientoRepository.findById(id);
    }
    /** Obtiene mantenimientos de un vehiculo concreto. */
    public List<Mantenimiento> listarPorVehiculo(Integer vehiculoId) {
        return mantenimientoRepository.findByVehiculoId(vehiculoId);
    }
    /** Guarda o actualiza un mantenimiento. El estado se calcula automaticamente por fechas. */
    public Mantenimiento guardar(Mantenimiento mantenimiento) {
        mantenimiento.setEstado(mantenimiento.calcularEstadoPorFecha());
        return mantenimientoRepository.save(mantenimiento);
    }
    /** Elimina un mantenimiento por su ID. */
    public void eliminar(Integer id) {
        mantenimientoRepository.deleteById(id);
    }
}

