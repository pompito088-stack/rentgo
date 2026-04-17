package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Mantenimiento;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.MantenimientoService;
import com.ilerna.rentgo.service.VehiculoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador CRUD para Mantenimiento.
 * Solo accesible por administradores.
 */
@Controller
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;
    private final VehiculoService vehiculoService;

    public MantenimientoController(MantenimientoService mantenimientoService,
                                   VehiculoService vehiculoService) {
        this.mantenimientoService = mantenimientoService;
        this.vehiculoService = vehiculoService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("mantenimientos", mantenimientoService.listarTodos());
        return "mantenimientos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("mantenimiento", new Mantenimiento());
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        return "mantenimientos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Mantenimiento> mantenimiento = mantenimientoService.buscarPorId(id);
        if (mantenimiento.isPresent()) {
            model.addAttribute("mantenimiento", mantenimiento.get());
            model.addAttribute("vehiculos", vehiculoService.listarTodos());
            return "mantenimientos/formulario";
        }
        return "redirect:/mantenimientos";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Mantenimiento mantenimiento,
                          BindingResult result,
                          Model model,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";

        if (result.hasErrors()) {
            model.addAttribute("vehiculos", vehiculoService.listarTodos());
            return "mantenimientos/formulario";
        }

        // Validar que fecha fin sea posterior a fecha inicio
        if (mantenimiento.getFechaFin() != null && mantenimiento.getFechaInicio() != null
                && mantenimiento.getFechaFin().isBefore(mantenimiento.getFechaInicio())) {
            result.rejectValue("fechaFin", "error.mantenimiento", "La fecha de fin debe ser posterior a la de inicio.");
            model.addAttribute("vehiculos", vehiculoService.listarTodos());
            return "mantenimientos/formulario";
        }

        mantenimientoService.guardar(mantenimiento);
        return "redirect:/mantenimientos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        mantenimientoService.eliminar(id);
        return "redirect:/mantenimientos";
    }
}

