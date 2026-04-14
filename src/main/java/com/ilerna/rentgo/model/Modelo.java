package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
/**
 * Entidad que representa la tabla modelos en la base de datos.
 * Cada modelo pertenece a una marca (ej: Corolla pertenece a Toyota).
 */
@Entity
@Table(name = "modelos")
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del modelo es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Relacion ManyToOne con Marca.
     * Muchos modelos pueden pertenecer a la misma marca.
     */
    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    // Constructor vacio (obligatorio para JPA)
    public Modelo() {
    }
    // Constructor con parametros
    public Modelo(Integer id, String nombre, Marca marca) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
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
    public Marca getMarca() {
        return marca;
    }
    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    @Override
    public String toString() {
        return marca.getNombre() + " " + nombre;
    }
}

