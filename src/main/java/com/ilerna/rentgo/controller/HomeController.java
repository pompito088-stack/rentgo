package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la pagina de inicio.
 * Redirige al login si no hay sesion activa.
 * Muestra dashboard distinto segun el rol (admin / cliente).
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        // Si no hay sesion se muestra la landing page (sin redirigir al login)
        model.addAttribute("usuario", usuario);
        model.addAttribute("esAdmin", usuario != null && usuario.isAdmin());
        return "index";
    }
}
