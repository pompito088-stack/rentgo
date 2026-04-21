package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
/**
 * Entidad que representa la tabla reservas en la base de datos.
 * Operacion principal del negocio: une usuario, vehiculo y sucursales con fechas.
 */
@Entity
@Table(name = "reservas")
public class Reserva {
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
    private LocalTime horaFin = LocalTime.of(9, 0);

    @NotNull(message = "La fecha de reserva es obligatoria")
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @NotNull(message = "El precio total es obligatorio")
    @DecimalMin(value = "0.00", message = "El precio total no puede ser negativo")
    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false)
    private String estado;

    @Size(max = 300, message = "Las observaciones no pueden superar 300 caracteres")
    @Column(name = "observaciones", length = 300)
    private String observaciones;

    /** Relacion ManyToOne con Usuario. */
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /** Relacion ManyToOne con Vehiculo. */
    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    /** Sucursal de recogida. */
    @ManyToOne
    @JoinColumn(name = "id_sucursal_recogida", nullable = false)
    private Sucursal sucursalRecogida;

    /** Sucursal de devolucion. */
    @ManyToOne
    @JoinColumn(name = "id_sucursal_devolucion", nullable = false)
    private Sucursal sucursalDevolucion;

    /**
     * Extras elegidos por el cliente al hacer la reserva.
     * Tabla intermedia reservas_extras.
     */
    @ManyToMany
    @JoinTable(
        name = "reservas_extras",
        joinColumns = @JoinColumn(name = "id_reserva"),
        inverseJoinColumns = @JoinColumn(name = "id_extra")
    )
    private Set<Extra> extras = new HashSet<>();

    // Constructor vacio (obligatorio para JPA)
    public Reserva() {
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
    public LocalDate getFechaReserva() {
        return fechaReserva;
    }
    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }
    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    public Sucursal getSucursalRecogida() {
        return sucursalRecogida;
    }
    public void setSucursalRecogida(Sucursal sucursalRecogida) {
        this.sucursalRecogida = sucursalRecogida;
    }
    public Sucursal getSucursalDevolucion() {
        return sucursalDevolucion;
    }
    public void setSucursalDevolucion(Sucursal sucursalDevolucion) {
        this.sucursalDevolucion = sucursalDevolucion;
    }
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    public LocalTime getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    public Set<Extra> getExtras() {
        return extras;
    }
    public void setExtras(Set<Extra> extras) {
        this.extras = extras;
    }
    /**
     * Retorna true si la reserva (confirmada o pendiente) ya ha pasado su fecha de fin.
     * Usado en la vista para mostrar el estado "finalizada".
     */
    public boolean isEstaFinalizada() {
        return ("confirmada".equals(estado) || "pendiente".equals(estado) || "en_proceso".equals(estado))
                && fechaFin != null
                && fechaFin.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Reserva #" + id + " (" + estado + ")";
    }
}

