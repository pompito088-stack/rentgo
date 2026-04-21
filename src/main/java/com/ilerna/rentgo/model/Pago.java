package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * Entidad que representa la tabla pagos en la base de datos.
 * Cada pago esta asociado a una unica reserva (relacion 1:1).
 * El estado_pago puede ser 'realizado' o 'reembolsado'.
 * Cuando se aprueba un reembolso se crea una Devolucion asociada.
 */
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La fecha de pago es obligatoria")
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe debe ser mayor que 0")
    @Column(name = "importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @NotNull(message = "La fianza es obligatoria")
    @DecimalMin(value = "0.00", message = "La fianza no puede ser negativa")
    @Column(name = "fianza", nullable = false, precision = 10, scale = 2)
    private BigDecimal fianza = BigDecimal.ZERO;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    /** Estado fijo: 'realizado' al crear el pago; cambia a 'reembolsado' si hay devolucion. */
    @Column(name = "estado_pago", nullable = false)
    private String estadoPago = "realizado";

    /** Relacion OneToOne con Reserva. Cada pago pertenece a una unica reserva. */
    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    /**
     * Relacion inversa con Devolucion (puede ser null si no existe reembolso).
     * mappedBy indica que Devolucion es el lado propietario de la FK.
     */
    @OneToOne(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private Devolucion devolucion;

    // Constructor vacio (obligatorio para JPA)
    public Pago() {
    }
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDate getFechaPago() {
        return fechaPago;
    }
    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }
    public BigDecimal getImporte() {
        return importe;
    }
    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
    public BigDecimal getFianza() {
        return fianza;
    }
    public void setFianza(BigDecimal fianza) {
        this.fianza = fianza;
    }
    public String getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    public String getEstadoPago() {
        return estadoPago;
    }
    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
    public Reserva getReserva() {
        return reserva;
    }
    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
    public Devolucion getDevolucion() {
        return devolucion;
    }
    public void setDevolucion(Devolucion devolucion) {
        this.devolucion = devolucion;
    }
    @Override
    public String toString() {
        return "Pago #" + id + " - " + importe + "€ (" + estadoPago + ")";
    }
}
