package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 * Entidad que representa la tabla mantenimientos en la base de datos.
 * Registra periodos de indisponibilidad tecnica de un vehiculo.
 */
@Entity
@Table(name = "mantenimientos")
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio = LocalTime.of(9, 0);

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin = LocalTime.of(18, 0);

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 200, message = "El motivo no puede superar 200 caracteres")
    @Column(name = "motivo", nullable = false, length = 200)
    private String motivo;

    @NotNull(message = "El coste es obligatorio")
    @DecimalMin(value = "0.00", message = "El coste no puede ser negativo")
    @Column(name = "coste", nullable = false, precision = 10, scale = 2)
    private BigDecimal coste;

    @Column(name = "estado", nullable = false)
    private String estado;

    /** Relacion ManyToOne con Vehiculo. */
    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    // Constructor vacio (obligatorio para JPA)
    public Mantenimiento() {
    }
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public BigDecimal getCoste() {
        return coste;
    }
    public void setCoste(BigDecimal coste) {
        this.coste = coste;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Calcula el estado segun la fecha+hora actual y las fechas/horas del mantenimiento.
     * pendiente  → ahora es anterior a fechaInicio+horaInicio
     * en_proceso → ahora esta entre inicio y fin
     * finalizado → ahora es posterior a fechaFin+horaFin
     */
    public String calcularEstadoPorFecha() {
        if (fechaInicio == null || fechaFin == null) return "pendiente";
        LocalTime hi = horaInicio != null ? horaInicio : LocalTime.of(9, 0);
        LocalTime hf = horaFin != null ? horaFin : LocalTime.of(18, 0);
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime ini = LocalDateTime.of(fechaInicio, hi);
        LocalDateTime fin = LocalDateTime.of(fechaFin, hf);
        if (ahora.isBefore(ini)) return "pendiente";
        if (ahora.isAfter(fin)) return "finalizado";
        return "en_proceso";
    }
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    @Override
    public String toString() {
        return "Mantenimiento #" + id + " - " + motivo + " (" + estado + ")";
    }
}
