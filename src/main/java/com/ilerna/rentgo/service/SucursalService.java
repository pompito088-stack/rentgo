package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Sucursal;
import com.ilerna.rentgo.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Sucursal.
 */
@Service
public class SucursalService {
    private final SucursalRepository sucursalRepository;

    public SucursalService(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }
    /** Obtiene todas las sucursales. */
    public List<Sucursal> listarTodas() {
        return sucursalRepository.findAll();
    }
    /** Busca una sucursal por su ID. */
    public Optional<Sucursal> buscarPorId(Integer id) {
        return sucursalRepository.findById(id);
    }
    /** Guarda o actualiza una sucursal. */
    public Sucursal guardar(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }
    /** Elimina una sucursal por su ID. */
    public void eliminar(Integer id) {
        sucursalRepository.deleteById(id);
    }
}

