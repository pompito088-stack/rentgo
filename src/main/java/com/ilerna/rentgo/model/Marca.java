package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
/**
 * Entidad que representa la tabla marcas en la base de datos.
 * Almacena las marcas de vehiculos (ej: Toyota, Ford, Seat).
 */
@Entity
@Table(name = "marcas")
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    // Constructor vacio (obligatorio para JPA)
    public Marca() {
    }
    // Constructor con parametros
    public Marca(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
    @Override
    public String toString() {
        return nombre;
    }
}

