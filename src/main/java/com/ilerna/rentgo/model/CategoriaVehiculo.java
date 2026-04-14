package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
/**
 * Entidad que representa la tabla categorias_vehiculos en la base de datos.
 * Agrupa vehiculos por tipo o segmento (ej: SUV, Sedan, Furgoneta).
 */
@Entity
@Table(name = "categorias_vehiculos")
public class CategoriaVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 200, message = "La descripcion no puede superar 200 caracteres")
    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    // Constructor vacio (obligatorio para JPA)
    public CategoriaVehiculo() {
    }
    // Constructor con parametros
    public CategoriaVehiculo(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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
    @Override
    public String toString() {
        return nombre;
    }
}

