package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Reserva;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controlador CRUD para Reserva.
 * Admin: ve todas las reservas. Cliente: solo las suyas.
 */
@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final VehiculoService vehiculoService;
    private final SucursalService sucursalService;
    private final UsuarioService usuarioService;

    public ReservaController(ReservaService reservaService,
                             VehiculoService vehiculoService,
                             SucursalService sucursalService,
                             UsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.vehiculoService = vehiculoService;
        this.sucursalService = sucursalService;
        this.usuarioService = usuarioService;
    }

    private boolean noEstaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    /** GET /reservas - Lista reservas segun rol. */
    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (logueado.isAdmin()) {
            model.addAttribute("reservas", reservaService.listarTodas());
        } else {
            model.addAttribute("reservas", reservaService.listarPorUsuario(logueado.getId()));
        }
        model.addAttribute("esAdmin", logueado.isAdmin());
        return "reservas/lista";
    }

    /** GET /reservas/nuevo - Formulario para crear reserva. */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.now());
        reserva.setEstado("pendiente");
        model.addAttribute("reserva", reserva);
        cargarDatosFormulario(model, session);
        return "reservas/formulario";
    }

    /** GET /reservas/editar/{id} - Formulario para editar reserva (solo admin). */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Reserva> reserva = reservaService.buscarPorId(id);
        if (reserva.isPresent()) {
            model.addAttribute("reserva", reserva.get());
            cargarDatosFormulario(model, session);
            return "reservas/formulario";
        }
        return "redirect:/reservas";
    }

    /** POST /reservas/guardar - Guarda o actualiza una reserva. */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Reserva reserva,
                          BindingResult result,
                          Model model,
                          HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (result.hasErrors()) {
            cargarDatosFormulario(model, session);
            return "reservas/formulario";
        }

        // Validar que fecha fin sea posterior a fecha inicio
        if (reserva.getFechaFin() != null && reserva.getFechaInicio() != null
                && reserva.getFechaFin().isBefore(reserva.getFechaInicio())) {
            result.rejectValue("fechaFin", "error.reserva", "La fecha de fin debe ser posterior a la de inicio.");
            cargarDatosFormulario(model, session);
            return "reservas/formulario";
        }

        // Validar solapamiento de reservas
        if (reserva.getVehiculo() != null && reserva.getFechaInicio() != null && reserva.getFechaFin() != null) {
            boolean solapada;
            if (reserva.getId() != null) {
                solapada = reservaService.existeSolapamiento(
                        reserva.getVehiculo().getId(), reserva.getFechaInicio(), reserva.getFechaFin(), reserva.getId());
            } else {
                solapada = reservaService.existeSolapamiento(
                        reserva.getVehiculo().getId(), reserva.getFechaInicio(), reserva.getFechaFin());
            }
            if (solapada) {
                result.rejectValue("vehiculo", "error.reserva", "El vehiculo ya tiene una reserva en esas fechas.");
                cargarDatosFormulario(model, session);
                return "reservas/formulario";
            }
        }

        // Si es cliente, asignar el usuario logueado
        if (!logueado.isAdmin()) {
            reserva.setUsuario(logueado);
        }

        // Establecer fecha de reserva si es nueva
        if (reserva.getId() == null) {
            reserva.setFechaReserva(LocalDate.now());
        }

        reservaService.guardar(reserva);
        return "redirect:/reservas";
    }

    /** GET /reservas/eliminar/{id} - Elimina una reserva (solo admin). */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        reservaService.eliminar(id);
        return "redirect:/reservas";
    }

    /** Carga datos para los selects del formulario. */
    private void cargarDatosFormulario(Model model, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("sucursales", sucursalService.listarTodas());
        model.addAttribute("esAdmin", logueado != null && logueado.isAdmin());
        if (logueado != null && logueado.isAdmin()) {
            model.addAttribute("usuarios", usuarioService.listarTodos());
        }
        model.addAttribute("estados", new String[]{"pendiente", "confirmada", "cancelada", "finalizada"});
    }
}

