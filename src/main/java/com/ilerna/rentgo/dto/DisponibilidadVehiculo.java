package com.ilerna.rentgo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO con el estado de disponibilidad calculado en tiempo real para un vehiculo.
 *  - estado: "disponible" | "alquilado" | "mantenimiento"
 *  - disponibleDesde: fecha+hora exacta en la que el vehiculo volvera a estar disponible (null si ya lo esta)
 */
public class DisponibilidadVehiculo {
    private final String estado;
    private final LocalDateTime disponibleDesde;

    public DisponibilidadVehiculo(String estado, LocalDateTime disponibleDesde) {
        this.estado = estado;
        this.disponibleDesde = disponibleDesde;
    }

    public String getEstado() { return estado; }
    public LocalDateTime getDisponibleDesde() { return disponibleDesde; }
    public boolean isDisponible() { return "disponible".equals(estado); }

    /** Fecha (sin hora) en la que vuelve a estar disponible. */
    public LocalDate getDisponibleFecha() {
        return disponibleDesde != null ? disponibleDesde.toLocalDate() : null;
    }
    /** Hora (HH:mm) en la que vuelve a estar disponible. */
    public String getDisponibleHora() {
        if (disponibleDesde == null) return null;
        return String.format("%02d:%02d", disponibleDesde.getHour(), disponibleDesde.getMinute());
    }
}
