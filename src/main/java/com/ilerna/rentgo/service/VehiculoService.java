package com.ilerna.rentgo.service;
import com.ilerna.rentgo.dto.DisponibilidadVehiculo;
import com.ilerna.rentgo.model.Mantenimiento;
import com.ilerna.rentgo.model.Reserva;
import com.ilerna.rentgo.model.Vehiculo;
import com.ilerna.rentgo.repository.MantenimientoRepository;
import com.ilerna.rentgo.repository.ReservaRepository;
import com.ilerna.rentgo.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * Servicio para la entidad Vehiculo.
 */
@Service
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;
    private final ReservaRepository reservaRepository;
    private final MantenimientoRepository mantenimientoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository,
                           ReservaRepository reservaRepository,
                           MantenimientoRepository mantenimientoRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.reservaRepository = reservaRepository;
        this.mantenimientoRepository = mantenimientoRepository;
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

    /**
     * Calcula la disponibilidad real de un vehiculo AHORA (fecha+hora) mirando reservas
     * (estados confirmada/pendiente/en_proceso) y mantenimientos vigentes.
     * Si esta ocupado, devuelve la fecha+hora exacta en la que volvera a estar disponible.
     */
    public DisponibilidadVehiculo calcularDisponibilidad(Integer vehiculoId) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate hoy = ahora.toLocalDate();

        // Mantenimiento activo (cuya ventana fecha+hora cubra el momento actual)
        List<Mantenimiento> mants = mantenimientoRepository.findByVehiculoId(vehiculoId);
        LocalDateTime finMantenimiento = null;
        for (Mantenimiento m : mants) {
            if (m.getFechaInicio() == null || m.getFechaFin() == null) continue;
            LocalTime hi = m.getHoraInicio() != null ? m.getHoraInicio() : LocalTime.of(9, 0);
            LocalTime hf = m.getHoraFin()    != null ? m.getHoraFin()    : LocalTime.of(18, 0);
            LocalDateTime ini = LocalDateTime.of(m.getFechaInicio(), hi);
            LocalDateTime fin = LocalDateTime.of(m.getFechaFin(), hf);
            if (!ahora.isBefore(ini) && !ahora.isAfter(fin)) {
                if (finMantenimiento == null || fin.isAfter(finMantenimiento)) finMantenimiento = fin;
            }
        }

        // Reserva activa (en curso ahora mismo, mirando hora)
        List<Reserva> reservas = reservaRepository.findReservasSolapadas(vehiculoId, hoy, hoy);
        LocalDateTime finReserva = null;
        for (Reserva r : reservas) {
            if ("cancelada".equals(r.getEstado()) || "finalizada".equals(r.getEstado())) continue;
            if (r.getFechaInicio() == null || r.getFechaFin() == null) continue;
            LocalTime hi = r.getHoraInicio() != null ? r.getHoraInicio() : LocalTime.of(9, 0);
            LocalTime hf = r.getHoraFin()    != null ? r.getHoraFin()    : LocalTime.of(9, 0);
            LocalDateTime ini = LocalDateTime.of(r.getFechaInicio(), hi);
            LocalDateTime fin = LocalDateTime.of(r.getFechaFin(), hf);
            if (!ahora.isBefore(ini) && !ahora.isAfter(fin)) {
                if (finReserva == null || fin.isAfter(finReserva)) finReserva = fin;
            }
        }

        if (finMantenimiento != null && (finReserva == null || finMantenimiento.isAfter(finReserva))) {
            return new DisponibilidadVehiculo("mantenimiento", finMantenimiento);
        }
        if (finReserva != null) {
            return new DisponibilidadVehiculo("alquilado", finReserva);
        }
        return new DisponibilidadVehiculo("disponible", null);
    }

    /** Calcula la disponibilidad de muchos vehiculos a la vez (mapa por id). */
    public Map<Integer, DisponibilidadVehiculo> calcularDisponibilidades(List<Vehiculo> vehiculos) {
        Map<Integer, DisponibilidadVehiculo> mapa = new HashMap<>();
        for (Vehiculo v : vehiculos) {
            mapa.put(v.getId(), calcularDisponibilidad(v.getId()));
        }
        return mapa;
    }
}
