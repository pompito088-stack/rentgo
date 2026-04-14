package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Reserva;
import com.ilerna.rentgo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 * Servicio para la entidad Reserva.
 */
@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }
    /** Obtiene todas las reservas. */
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }
    /** Busca una reserva por su ID. */
    public Optional<Reserva> buscarPorId(Integer id) {
        return reservaRepository.findById(id);
    }
    /** Obtiene las reservas de un usuario concreto. */
    public List<Reserva> listarPorUsuario(Integer usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }
    /** Comprueba si hay reservas solapadas para un vehiculo en un rango de fechas. */
    public boolean existeSolapamiento(Integer vehiculoId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Reserva> solapadas = reservaRepository.findReservasSolapadas(vehiculoId, fechaInicio, fechaFin);
        return !solapadas.isEmpty();
    }
    /** Comprueba solapamiento excluyendo una reserva concreta (para edicion). */
    public boolean existeSolapamiento(Integer vehiculoId, LocalDate fechaInicio, LocalDate fechaFin, Integer reservaIdExcluir) {
        List<Reserva> solapadas = reservaRepository.findReservasSolapadas(vehiculoId, fechaInicio, fechaFin);
        return solapadas.stream().anyMatch(r -> !r.getId().equals(reservaIdExcluir));
    }
    /** Guarda o actualiza una reserva. */
    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }
    /** Elimina una reserva por su ID. */
    public void eliminar(Integer id) {
        reservaRepository.deleteById(id);
    }
}

