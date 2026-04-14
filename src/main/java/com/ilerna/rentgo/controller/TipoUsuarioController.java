package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.TipoUsuario;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.TipoUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador CRUD para TipoUsuario.
 * Solo accesible por administradores.
 */
@Controller
@RequestMapping("/tipo-usuarios")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    /** Comprueba que el usuario logueado es admin. */
    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    /** GET /tipo-usuarios - Lista todos los tipos de usuario (solo admin). */
    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("tiposUsuario", tipoUsuarioService.listarTodos());
        return "tipo-usuarios/lista";
    }

    /** GET /tipo-usuarios/nuevo - Muestra formulario para crear (solo admin). */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("tipoUsuario", new TipoUsuario());
        return "tipo-usuarios/formulario";
    }

    /** GET /tipo-usuarios/editar/{id} - Muestra formulario para editar (solo admin). */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<TipoUsuario> tipoUsuario = tipoUsuarioService.buscarPorId(id);
        if (tipoUsuario.isPresent()) {
            model.addAttribute("tipoUsuario", tipoUsuario.get());
            return "tipo-usuarios/formulario";
        }
        return "redirect:/tipo-usuarios";
    }

    /** POST /tipo-usuarios/guardar - Guarda o actualiza un tipo de usuario (solo admin). */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute TipoUsuario tipoUsuario, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        tipoUsuarioService.guardar(tipoUsuario);
        return "redirect:/tipo-usuarios";
    }

    /** GET /tipo-usuarios/eliminar/{id} - Elimina un tipo de usuario (solo admin). */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        tipoUsuarioService.eliminar(id);
        return "redirect:/tipo-usuarios";
    }
}
