package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la seccion "Mi perfil" del usuario autenticado.
 */
@Controller
public class PerfilController {

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
}

