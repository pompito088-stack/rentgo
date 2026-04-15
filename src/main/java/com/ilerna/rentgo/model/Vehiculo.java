package com.ilerna.rentgo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
/**
 * Entidad que representa la tabla vehiculos en la base de datos.
 * Cada vehiculo tiene un modelo, una categoria y esta asignado a una sucursal.
 * Relacion M:N con Extra a traves de la tabla vehiculos_extras.
 */
@Entity
@Table(name = "vehiculos")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La matricula es obligatoria")
    @Size(max = 10, message = "La matricula no puede superar 10 caracteres")
    @Column(name = "matricula", nullable = false, unique = true, length = 10)
    private String matricula;

    @NotNull(message = "El anio es obligatorio")
    @Min(value = 1990, message = "El anio minimo es 1990")
    @Max(value = 2030, message = "El anio maximo es 2030")
    @Column(name = "anio", nullable = false)
    private Integer anio;

    @NotBlank(message = "El color es obligatorio")
    @Size(max = 30, message = "El color no puede superar 30 caracteres")
    @Column(name = "color", nullable = false, length = 30)
    private String color;

    @NotNull(message = "Las plazas son obligatorias")
    @Min(value = 1, message = "El vehiculo debe tener al menos 1 plaza")
    @Max(value = 9, message = "El vehiculo no puede tener mas de 9 plazas")
    @Column(name = "plazas", nullable = false)
    private Byte plazas;

    @NotNull(message = "Las puertas son obligatorias")
    @Min(value = 1, message = "El vehiculo debe tener al menos 1 puerta")
    @Max(value = 6, message = "El vehiculo no puede tener mas de 6 puertas")
    @Column(name = "puertas", nullable = false)
    private Byte puertas;

    @NotBlank(message = "La etiqueta es obligatoria")
    @Pattern(regexp = "^(sin_etiqueta|B|C|ECO|CERO)$",
            message = "La etiqueta debe ser sin_etiqueta, B, C, ECO o CERO")
    @Column(name = "etiqueta", nullable = false)
    private String etiqueta;

    @NotBlank(message = "La transmision es obligatoria")
    @Pattern(regexp = "^(manual|automatica)$",
            message = "La transmision debe ser manual o automatica")
    @Column(name = "transmision", nullable = false)
    private String transmision;

    @NotBlank(message = "La combustion es obligatoria")
    @Pattern(
        regexp = "^(gasolina|diesel|hibrido_gasolina|hibrido_diesel|hibrido_enchufable_gasolina|hibrido_enchufable_diesel|electrico|glp|gnc|hidrogeno)$",
        message = "Selecciona un tipo de combustion valido"
    )
    @Column(name = "combustion", nullable = false)
    private String combustion;

    @NotNull(message = "El kilometraje es obligatorio")
    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    @Column(name = "kilometraje", nullable = false)
    private Integer kilometraje;

    @NotNull(message = "El precio por dia es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    @Column(name = "precio_dia", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioDia;

    @Column(name = "ruta_foto", length = 255)
    private String rutaFoto;

    /** Relacion ManyToOne con CategoriaVehiculo. */
    @ManyToOne
    @JoinColumn(name = "id_categoria_vehiculo", nullable = false)
    private CategoriaVehiculo categoriaVehiculo;

    /** Relacion ManyToOne con Modelo. */
    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private Modelo modelo;

    /** Relacion ManyToOne con Sucursal. */
    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = false)
    private Sucursal sucursal;

    /**
     * Relacion ManyToMany con Extra.
     * Tabla intermedia vehiculos_extras.
     */
    @ManyToMany
    @JoinTable(
        name = "vehiculos_extras",
        joinColumns = @JoinColumn(name = "id_vehiculo"),
        inverseJoinColumns = @JoinColumn(name = "id_extra")
    )
    private Set<Extra> extras = new HashSet<>();

    // Constructor vacio (obligatorio para JPA)
    public Vehiculo() {
    }
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public Integer getAnio() {
        return anio;
    }
    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public Byte getPlazas() {
        return plazas;
    }
    public void setPlazas(Byte plazas) {
        this.plazas = plazas;
    }
    public Byte getPuertas() {
        return puertas;
    }
    public void setPuertas(Byte puertas) {
        this.puertas = puertas;
    }
    public String getEtiqueta() {
        return etiqueta;
    }
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    public String getTransmision() {
        return transmision;
    }
    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }
    public String getCombustion() {
        return combustion;
    }
    public void setCombustion(String combustion) {
        this.combustion = combustion;
    }
    public Integer getKilometraje() {
        return kilometraje;
    }
    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }
    public BigDecimal getPrecioDia() {
        return precioDia;
    }
    public void setPrecioDia(BigDecimal precioDia) {
        this.precioDia = precioDia;
    }
    public String getRutaFoto() {
        return rutaFoto;
    }
    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }
    public CategoriaVehiculo getCategoriaVehiculo() {
        return categoriaVehiculo;
    }
    public void setCategoriaVehiculo(CategoriaVehiculo categoriaVehiculo) {
        this.categoriaVehiculo = categoriaVehiculo;
    }
    public Modelo getModelo() {
        return modelo;
    }
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    public Sucursal getSucursal() {
        return sucursal;
    }
    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }
    public Set<Extra> getExtras() {
        return extras;
    }
    public void setExtras(Set<Extra> extras) {
        this.extras = extras;
    }
    @Override
    public String toString() {
        return modelo + " - " + matricula;
    }
}

