package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

/**
 * Controlador para la seccion "Mi perfil" del usuario autenticado.
 */
@Controller
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/mi-perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("esAdmin", usuario.isAdmin());
        return "usuarios/perfil";
    }

    /** GET /mi-perfil/editar - Formulario de edicion de perfil (solo clientes). */
    @GetMapping("/mi-perfil/editar")
    public String mostrarFormularioEditar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";
        if (usuario.isAdmin()) return "redirect:/mi-perfil";
        model.addAttribute("usuario", usuario);
        return "usuarios/editar-perfil";
    }

    /** POST /mi-perfil/guardar - Guarda los cambios de perfil del cliente. */
    @PostMapping("/mi-perfil/guardar")
    public String guardarPerfil(@ModelAttribute Usuario usuarioForm,
                                HttpSession session,
                                Model model) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (logueado == null) return "redirect:/login";
        if (logueado.isAdmin()) return "redirect:/mi-perfil";

        // Cargar el usuario completo de la BD para no perder datos sensibles
        Optional<Usuario> opt = usuarioService.buscarPorId(logueado.getId());
        if (opt.isEmpty()) return "redirect:/login";

        Usuario usuarioBD = opt.get();

        // Comprobar telefono duplicado (excluyendo el propio)
        Optional<Usuario> existente = usuarioService.buscarPorTelefono(usuarioForm.getTelefono());
        if (existente.isPresent() && !existente.get().getId().equals(usuarioBD.getId())) {
            model.addAttribute("usuario", usuarioForm);
            model.addAttribute("error", "Este telefono ya esta registrado por otro usuario.");
            return "usuarios/editar-perfil";
        }

        // Actualizar solo los campos editables
        usuarioBD.setNombre(usuarioForm.getNombre());
        usuarioBD.setApellidos(usuarioForm.getApellidos());
        usuarioBD.setTelefono(usuarioForm.getTelefono());
        usuarioBD.setDireccion(usuarioForm.getDireccion());

        Usuario actualizado = usuarioService.guardar(usuarioBD);

        // Actualizar la sesion con los nuevos datos
        session.setAttribute("usuarioLogueado", actualizado);

        return "redirect:/mi-perfil";
    }
}
