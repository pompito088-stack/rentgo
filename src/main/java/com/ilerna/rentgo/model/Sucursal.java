package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
/**
 * Entidad que representa la tabla sucursales en la base de datos.
 * Representa los puntos fisicos donde se recogen y devuelven vehiculos.
 */
@Entity
@Table(name = "sucursales")
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 200, message = "La direccion no puede superar 200 caracteres")
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 80, message = "La ciudad no puede superar 80 caracteres")
    @Column(name = "ciudad", nullable = false, length = 80)
    private String ciudad;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    @Size(max = 100, message = "El email no puede superar 100 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[69]\\d{8}$", message = "El telefono debe empezar por 6 o 9 y tener 9 digitos")
    @Column(name = "telefono", nullable = false, unique = true, length = 9)
    private String telefono;

    // Constructor vacio (obligatorio para JPA)
    public Sucursal() {
    }
    // Constructor con parametros
    public Sucursal(Integer id, String nombre, String direccion, String ciudad, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.email = email;
        this.telefono = telefono;
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
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    @Override
    public String toString() {
        return nombre + " (" + ciudad + ")";
    }
}

