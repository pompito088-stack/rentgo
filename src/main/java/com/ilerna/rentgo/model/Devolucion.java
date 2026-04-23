package com.ilerna.rentgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa la tabla devoluciones.
 * Relacion 1:1 con Pago: un pago solo puede tener una devolucion asociada.
 * Se crea cuando el admin aprueba el reembolso de un pago cancelado.
 */
@Entity
@Table(name = "devoluciones")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La fecha de devolucion es obligatoria")
    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDate fechaDevolucion;

    @NotNull(message = "El importe reembolsado es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe debe ser mayor que 0")
    @Column(name = "importe_reembolsado", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeReembolsado;

    @Size(max = 300, message = "El motivo no puede superar los 300 caracteres")
    @Column(name = "motivo")
    private String motivo;

    /**
     * Relacion OneToOne con Pago.
     * Al guardar la devolucion tambien cambiamos el estado del pago a 'reembolsado'.
     */
    @OneToOne
    @JoinColumn(name = "id_pago", nullable = false, unique = true)
    @NotNull(message = "El pago asociado es obligatorio")
    private Pago pago;

    // Constructor vacio requerido por JPA
    public Devolucion() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public BigDecimal getImporteReembolsado() { return importeReembolsado; }
    public void setImporteReembolsado(BigDecimal importeReembolsado) { this.importeReembolsado = importeReembolsado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    @Override
    public String toString() {
        return "Devolucion #" + id + " - " + importeReembolsado + "€ (" + fechaDevolucion + ")";
    }
}

