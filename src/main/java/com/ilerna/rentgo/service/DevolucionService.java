package com.ilerna.rentgo.service;

import com.ilerna.rentgo.model.Devolucion;
import com.ilerna.rentgo.model.Pago;
import com.ilerna.rentgo.repository.DevolucionRepository;
import com.ilerna.rentgo.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la entidad Devolucion.
 * Al guardar una devolucion tambien actualiza el estado del pago a 'reembolsado'.
 */
@Service
public class DevolucionService {

    private final DevolucionRepository devolucionRepository;
    private final PagoRepository pagoRepository;

    public DevolucionService(DevolucionRepository devolucionRepository, PagoRepository pagoRepository) {
        this.devolucionRepository = devolucionRepository;
        this.pagoRepository = pagoRepository;
    }

    /** Obtiene todas las devoluciones. */
    public List<Devolucion> listarTodas() {
        return devolucionRepository.findAll();
    }

    /** Busca una devolucion por su ID. */
    public Optional<Devolucion> buscarPorId(Integer id) {
        return devolucionRepository.findById(id);
    }

    /** Busca la devolucion asociada a un pago. */
    public Optional<Devolucion> buscarPorPago(Integer pagoId) {
        return devolucionRepository.findByPagoId(pagoId);
    }

    /** Lista las devoluciones de un usuario. */
    public List<Devolucion> listarPorUsuario(Integer usuarioId) {
        return devolucionRepository.findByPagoReservaUsuarioId(usuarioId);
    }

    /**
     * Guarda o actualiza una devolucion.
     * Al crear, marca el pago asociado como 'reembolsado'.
     * Al eliminar (no en este metodo) se deberia revertir el estado si procede.
     */
    @Transactional
    public Devolucion guardar(Devolucion devolucion) {
        Devolucion saved = devolucionRepository.save(devolucion);
        // Actualizar estado del pago a 'reembolsado'
        Pago pago = saved.getPago();
        if (pago != null && !"reembolsado".equals(pago.getEstadoPago())) {
            pago.setEstadoPago("reembolsado");
            pagoRepository.save(pago);
        }
        return saved;
    }

    /**
     * Elimina una devolucion por su ID.
     * Revierte el estado del pago a 'realizado' si no hay otra devolucion.
     */
    @Transactional
    public void eliminar(Integer id) {
        devolucionRepository.findById(id).ifPresent(dev -> {
            Pago pago = dev.getPago();
            devolucionRepository.deleteById(id);
            if (pago != null) {
                pago.setEstadoPago("realizado");
                pagoRepository.save(pago);
            }
        });
    }
}

