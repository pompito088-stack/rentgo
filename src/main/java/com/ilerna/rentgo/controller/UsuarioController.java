package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para Usuario (solo admin, solo lectura de lista).
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    /** GET /usuarios - Lista todos los usuarios (solo admin). */
    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    /** GET /usuarios/nuevo - Bloqueado. */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        return "redirect:/usuarios";
    }

    /** GET /usuarios/editar/{id} - Bloqueado. */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        return "redirect:/usuarios";
    }

    /** POST /usuarios/guardar - Bloqueado. */
    @PostMapping("/guardar")
    public String guardar(HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        return "redirect:/usuarios";
    }

    /** GET /usuarios/eliminar/{id} - Bloqueado. */
    @GetMapping("/eliminar/{id}")
    public String eliminar(HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        return "redirect:/usuarios";
    }
}
