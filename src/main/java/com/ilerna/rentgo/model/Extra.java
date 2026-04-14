package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
/**
 * Entidad que representa la tabla extras en la base de datos.
 * Extras opcionales que se pueden anadir a un vehiculo (ej: GPS, silla bebe).
 */
@Entity
@Table(name = "extras")
public class Extra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del extra es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 80)
    private String nombre;

    @Size(max = 200, message = "La descripcion no puede superar 200 caracteres")
    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @NotNull(message = "El precio por dia es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    @Column(name = "precio_dia", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioDia;

    // Constructor vacio (obligatorio para JPA)
    public Extra() {
    }
    // Constructor con parametros
    public Extra(Integer id, String nombre, String descripcion, BigDecimal precioDia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioDia = precioDia;
    }
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public BigDecimal getPrecioDia() {
        return precioDia;
    }
    public void setPrecioDia(BigDecimal precioDia) {
        this.precioDia = precioDia;
    }
    @Override
    public String toString() {
        return nombre;
    }
}

