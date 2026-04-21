package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Sucursal;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.SucursalService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador CRUD para Sucursal.
 * Solo accesible por administradores.
 */
@Controller
@RequestMapping("/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("sucursales", sucursalService.listarTodas());
        model.addAttribute("esAdmin", logueado != null && logueado.isAdmin());
        return "sucursales/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("sucursal", new Sucursal());
        return "sucursales/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Sucursal> sucursal = sucursalService.buscarPorId(id);
        if (sucursal.isPresent()) {
            model.addAttribute("sucursal", sucursal.get());
            return "sucursales/formulario";
        }
        return "redirect:/sucursales";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Sucursal sucursal,
                          BindingResult result,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        if (result.hasErrors()) return "sucursales/formulario";
        sucursalService.guardar(sucursal);
        return "redirect:/sucursales";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        sucursalService.eliminar(id);
        return "redirect:/sucursales";
    }
}

