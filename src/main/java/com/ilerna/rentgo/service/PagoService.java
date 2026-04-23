package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Pago;
import com.ilerna.rentgo.repository.PagoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Pago.
 */
@Service
public class PagoService {
    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }
    /** Obtiene todos los pagos. */
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }
    /** Busca un pago por su ID. */
    public Optional<Pago> buscarPorId(Integer id) {
        return pagoRepository.findById(id);
    }
    /** Busca el pago asociado a una reserva. */
    public Optional<Pago> buscarPorReserva(Integer reservaId) {
        return pagoRepository.findByReservaId(reservaId);
    }
    /** Busca todos los pagos de un usuario (a traves de sus reservas). */
    public List<Pago> listarPorUsuario(Integer usuarioId) {
        return pagoRepository.findByReservaUsuarioId(usuarioId);
    }
    /** Guarda o actualiza un pago. */
    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }
    /** Elimina un pago por su ID. */
    public void eliminar(Integer id) {
        pagoRepository.deleteById(id);
    }
}

