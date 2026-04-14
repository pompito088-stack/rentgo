package com.ilerna.rentgo.model;
import jakarta.persistence.*;
/**
 * Entidad que representa la tabla tipo_usuarios en la base de datos.
 * Almacena los tipos de usuario del sistema (ej: cliente, admin).
 */
@Entity
@Table(name = "tipo_usuarios")
public class TipoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;
    // Constructor vacio (obligatorio para JPA)
    public TipoUsuario() {
    }
    // Constructor con parametros
    public TipoUsuario(Integer id, String nombre) {
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
