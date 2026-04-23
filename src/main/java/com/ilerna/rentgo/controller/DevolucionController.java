package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Devolucion;
import com.ilerna.rentgo.model.Pago;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.DevolucionService;
import com.ilerna.rentgo.service.PagoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador CRUD para Devolucion.
 * Solo accesible por administradores.
 * Al crear/eliminar una devolucion se actualiza el estado del pago asociado.
 */
@Controller
@RequestMapping("/devoluciones")
public class DevolucionController {

    private final DevolucionService devolucionService;
    private final PagoService pagoService;

    public DevolucionController(DevolucionService devolucionService, PagoService pagoService) {
        this.devolucionService = devolucionService;
        this.pagoService = pagoService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    /** GET /devoluciones — Lista todas las devoluciones (solo admin) */
    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("devoluciones", devolucionService.listarTodas());
        model.addAttribute("activePage", "devoluciones");
        return "devoluciones/lista";
    }

    /** GET /devoluciones/nueva — Formulario de nueva devolucion */
    @GetMapping("/nueva")
    public String mostrarFormularioCrear(Model model, HttpSession session,
                                         @RequestParam(required = false) Integer pagoId) {
        if (noEsAdmin(session)) return "redirect:/login";
        Devolucion devolucion = new Devolucion();
        // Si llega pagoId preseleccionar el pago
        if (pagoId != null) {
            pagoService.buscarPorId(pagoId).ifPresent(devolucion::setPago);
        }
        model.addAttribute("devolucion", devolucion);
        // Solo pagos "realizado" sin devolucion previa son candidatos a reembolso
        List<Pago> pagosReembolsables = pagoService.listarTodos().stream()
                .filter(p -> "realizado".equals(p.getEstadoPago()) && p.getDevolucion() == null)
                .toList();
        model.addAttribute("pagosReembolsables", pagosReembolsables);
        model.addAttribute("activePage", "devoluciones");
        return "devoluciones/formulario";
    }

    /** GET /devoluciones/editar/{id} — Formulario de edicion */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Devolucion> devolucion = devolucionService.buscarPorId(id);
        if (devolucion.isEmpty()) return "redirect:/devoluciones";
        // Al editar mostramos el pago actual de esta devolucion (ya reembolsado)
        model.addAttribute("devolucion", devolucion.get());
        // Incluimos el pago actual para poder mostrarlo aunque este "reembolsado"
        List<Pago> pagosReembolsables = pagoService.listarTodos().stream()
                .filter(p -> ("realizado".equals(p.getEstadoPago()) && p.getDevolucion() == null)
                          || p.getId().equals(devolucion.get().getPago().getId()))
                .toList();
        model.addAttribute("pagosReembolsables", pagosReembolsables);
        model.addAttribute("activePage", "devoluciones");
        return "devoluciones/formulario";
    }

    /** POST /devoluciones/guardar — Guarda nueva devolucion o actualiza */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Devolucion devolucion,
                          BindingResult result,
                          Model model,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        if (result.hasErrors()) {
            List<Pago> pagosReembolsables = pagoService.listarTodos().stream()
                    .filter(p -> "realizado".equals(p.getEstadoPago()) && p.getDevolucion() == null)
                    .toList();
            model.addAttribute("pagosReembolsables", pagosReembolsables);
            model.addAttribute("activePage", "devoluciones");
            return "devoluciones/formulario";
        }
        // Resolver la entidad Pago completa a partir del id recibido en el form
        if (devolucion.getPago() != null && devolucion.getPago().getId() != null) {
            pagoService.buscarPorId(devolucion.getPago().getId())
                       .ifPresent(devolucion::setPago);
        }
        devolucionService.guardar(devolucion);
        return "redirect:/devoluciones";
    }

    /** GET /devoluciones/eliminar/{id} — Elimina y revierte el pago a 'realizado' */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        devolucionService.eliminar(id);
        return "redirect:/devoluciones";
    }
}

