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

import java.util.Optional;

/**
 * Controlador CRUD para Usuario.
 * Solo accesible por administradores.
 * El tipo de usuario se asigna automaticamente segun el patron del email.
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final TipoUsuarioService tipoUsuarioService;

    public UsuarioController(UsuarioService usuarioService, TipoUsuarioService tipoUsuarioService) {
        this.usuarioService = usuarioService;
        this.tipoUsuarioService = tipoUsuarioService;
    }

    /** Comprueba que el usuario logueado es admin; si no, redirige al login. */
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

    /** GET /usuarios/nuevo - Muestra formulario para crear un usuario (solo admin). */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    /** GET /usuarios/editar/{id} - Muestra formulario para editar un usuario (solo admin). */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuarios/formulario";
        }
        return "redirect:/usuarios";
    }

    /** POST /usuarios/guardar - Guarda o actualiza un usuario (solo admin). */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Usuario usuario,
                          BindingResult result,
                          Model model,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";

        if (result.hasErrors()) {
            return "usuarios/formulario";
        }

        // Comprobar email duplicado (excluyendo al propio usuario en edicion)
        Optional<Usuario> existente = usuarioService.buscarPorEmail(usuario.getEmail());
        if (existente.isPresent() && !existente.get().getId().equals(usuario.getId())) {
            result.rejectValue("email", "error.usuario", "Este email ya esta registrado.");
            return "usuarios/formulario";
        }

        // Comprobar telefono duplicado
        existente = usuarioService.buscarPorTelefono(usuario.getTelefono());
        if (existente.isPresent() && !existente.get().getId().equals(usuario.getId())) {
            result.rejectValue("telefono", "error.usuario", "Este telefono ya esta registrado.");
            return "usuarios/formulario";
        }

        // Comprobar DNI duplicado
        existente = usuarioService.buscarPorDni(usuario.getDni());
        if (existente.isPresent() && !existente.get().getId().equals(usuario.getId())) {
            result.rejectValue("dni", "error.usuario", "Este DNI ya esta registrado.");
            return "usuarios/formulario";
        }

        // Asignar tipo automaticamente segun patron del email
        if (usuario.getEmail() != null && usuario.getEmail().contains("@admin-")) {
            usuario.setTipoUsuario(tipoUsuarioService.buscarPorNombre("admin").orElse(null));
        } else {
            usuario.setTipoUsuario(tipoUsuarioService.buscarPorNombre("cliente").orElse(null));
        }

        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    /** GET /usuarios/eliminar/{id} - Elimina un usuario (solo admin). */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}
