package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
/**
 * Entidad que representa la tabla usuarios en la base de datos.
 * Cada usuario tiene un tipo (cliente o admin) a traves de la FK id_tipo.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    // Permite letras con tildes, guiones y apostrofes; exige al menos dos palabras separadas por espacio.
    @Pattern(regexp = "^[\\p{L}'\\-]+(?:\\s+[\\p{L}'\\-]+)+$",
             message = "Debe introducir al menos dos apellidos separados por un espacio")
    @Size(max = 80, message = "Los apellidos no pueden superar 80 caracteres")
    @Column(name = "apellidos", nullable = false, length = 80)
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato valido (contener @)")
    @Size(max = 100, message = "El email no puede superar 100 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[67]\\d{8}$",
             message = "El telefono debe empezar por 6 o 7 y tener 9 digitos")
    @Column(name = "telefono", nullable = false, unique = true, length = 9)
    private String telefono;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}[A-Za-z]$",
             message = "El DNI debe tener 8 digitos seguidos de una letra")
    @Column(name = "dni", nullable = false, unique = true, length = 9)
    private String dni;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 200, message = "La direccion no puede superar 200 caracteres")
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    @Column(name = "password", nullable = false)
    private String password;

    /** Ruta a la foto del carnet de conducir (opcional). */
    @Size(max = 255, message = "La ruta no puede superar 255 caracteres")
    @Column(name = "ruta_foto_carnet", length = 255)
    private String rutaFotoCarnet;

    /**
     * Relacion ManyToOne con TipoUsuario.
     * Muchos usuarios pueden tener el mismo tipo (ej: muchos "cliente").
     * La columna en la BD es id_tipo.
     */
    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoUsuario tipoUsuario;
    // Constructor vacio (obligatorio para JPA)
    public Usuario() {
    }
    // Constructor con parametros
    public Usuario(Integer id, String nombre, String apellidos, String email,
                   String telefono, String dni, String direccion, String password,
                   TipoUsuario tipoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.dni = dni;
        this.direccion = direccion;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
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
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRutaFotoCarnet() {
        return rutaFotoCarnet;
    }
    public void setRutaFotoCarnet(String rutaFotoCarnet) {
        this.rutaFotoCarnet = rutaFotoCarnet;
    }
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    /** Comprueba si el usuario es admin. */
    public boolean isAdmin() {
        return tipoUsuario != null && "admin".equals(tipoUsuario.getNombre());
    }
    @Override
    public String toString() {
        return nombre + " " + apellidos;
    }
}
