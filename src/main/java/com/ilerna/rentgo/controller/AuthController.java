package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.TipoUsuarioService;
import com.ilerna.rentgo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador de autenticacion: login, registro y logout.
 * No se usa Spring Security; se gestiona la sesion con HttpSession.
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final TipoUsuarioService tipoUsuarioService;

    public AuthController(UsuarioService usuarioService, TipoUsuarioService tipoUsuarioService) {
        this.usuarioService = usuarioService;
        this.tipoUsuarioService = tipoUsuarioService;
    }

    // ===================== LOGIN =====================

    /** GET /login - Muestra el formulario de inicio de sesion. */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    /** POST /login - Procesa el inicio de sesion (solo email y contrasena). */
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        Optional<Usuario> optUsuario = usuarioService.buscarPorEmail(email);

        if (optUsuario.isPresent() && optUsuario.get().getPassword().equals(password)) {
            session.setAttribute("usuarioLogueado", optUsuario.get());
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("error", "Email o contrasena incorrectos.");
        return "redirect:/login";
    }

    // ===================== REGISTRO =====================

    /** GET /registro - Muestra el formulario de registro (sign up). */
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    /** POST /registro - Procesa el registro de un nuevo cliente. */
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute Usuario usuario,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        // Validaciones de Bean Validation
        if (result.hasErrors()) {
            return "auth/registro";
        }

        // Bloquear emails con patron de admin (%@admin-%)
        if (usuario.getEmail() != null && usuario.getEmail().contains("@admin-")) {
            result.rejectValue("email", "error.usuario",
                    "No se permite registrar con un email de administrador.");
            return "auth/registro";
        }

        // Comprobar email duplicado
        if (usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            result.rejectValue("email", "error.usuario",
                    "Este email ya esta registrado.");
            return "auth/registro";
        }

        // Comprobar telefono duplicado
        if (usuarioService.buscarPorTelefono(usuario.getTelefono()).isPresent()) {
            result.rejectValue("telefono", "error.usuario",
                    "Este telefono ya esta registrado.");
            return "auth/registro";
        }

        // Comprobar DNI duplicado
        if (usuarioService.buscarPorDni(usuario.getDni()).isPresent()) {
            result.rejectValue("dni", "error.usuario",
                    "Este DNI ya esta registrado.");
            return "auth/registro";
        }

        // Asignar tipo "cliente" automaticamente
        usuario.setTipoUsuario(tipoUsuarioService.buscarPorNombre("cliente").orElse(null));

        usuarioService.guardar(usuario);

        redirectAttributes.addFlashAttribute("exito", "Registro exitoso. Ya puedes iniciar sesion.");
        return "redirect:/login";
    }

    // ===================== LOGOUT =====================

    /** GET /logout - Cierra la sesion del usuario. */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

