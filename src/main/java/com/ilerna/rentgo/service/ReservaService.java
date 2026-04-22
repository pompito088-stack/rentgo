package com.ilerna.rentgo.service;
import com.ilerna.rentgo.model.Reserva;
import com.ilerna.rentgo.repository.ReservaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    /** Obtiene las N reservas más recientes. */
    public List<Reserva> listarUltimas(int n) {
        return reservaRepository.findAllByOrderByFechaReservaDesc(PageRequest.of(0, n));
    }
    /** Elimina una reserva por su ID. */
    public void eliminar(Integer id) {
        reservaRepository.deleteById(id);
    }

    /**
     * Actualiza automáticamente los estados de las reservas en función de
     * la fecha + hora actual:
     *  - "confirmada"/"pendiente"  → "en_proceso"  cuando llega la hora de inicio.
     *  - "confirmada"/"pendiente"/"en_proceso" → "finalizada" cuando pasa la hora de fin.
     * Se ejecuta cada vez que se listan reservas para mantener coherencia en tiempo real.
     */
    public void actualizarEstadosAutomaticamente() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Reserva> todas = reservaRepository.findAll();
        for (Reserva r : todas) {
            String estado = r.getEstado();
            if (estado == null) continue;

            // 1) Finalizar si ya pasó fecha+hora de fin
            if (r.getFechaFin() != null
                    && ("confirmada".equals(estado) || "pendiente".equals(estado) || "en_proceso".equals(estado))) {
                LocalTime hf = r.getHoraFin() != null ? r.getHoraFin() : LocalTime.of(9, 0);
                LocalDateTime finDt = LocalDateTime.of(r.getFechaFin(), hf);
                if (finDt.isBefore(ahora) || finDt.isEqual(ahora)) {
                    r.setEstado("finalizada");
                    reservaRepository.save(r);
                    continue;
                }
            }

            // 2) Marcar en proceso si ya empezó pero aún no acabó
            if (r.getFechaInicio() != null
                    && ("confirmada".equals(estado) || "pendiente".equals(estado))) {
                LocalTime hi = r.getHoraInicio() != null ? r.getHoraInicio() : LocalTime.of(9, 0);
                LocalDateTime iniDt = LocalDateTime.of(r.getFechaInicio(), hi);
                if (!ahora.isBefore(iniDt)) {
                    r.setEstado("en_proceso");
                    reservaRepository.save(r);
                }
            }
        }
    }

    /**
     * @deprecated Usar {@link #actualizarEstadosAutomaticamente()}.
     * Mantenido por compatibilidad con llamadas existentes.
     */
    @Deprecated
    public void autoFinalizarExpiradas() {
        actualizarEstadosAutomaticamente();
    }
}

