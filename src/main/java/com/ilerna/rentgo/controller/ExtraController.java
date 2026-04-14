package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Extra;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.ExtraService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador CRUD para Extra.
 * Solo accesible por administradores.
 */
@Controller
@RequestMapping("/extras")
public class ExtraController {

    private final ExtraService extraService;

    public ExtraController(ExtraService extraService) {
        this.extraService = extraService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("extras", extraService.listarTodos());
        return "extras/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("extra", new Extra());
        return "extras/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Extra> extra = extraService.buscarPorId(id);
        if (extra.isPresent()) {
            model.addAttribute("extra", extra.get());
            return "extras/formulario";
        }
        return "redirect:/extras";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Extra extra,
                          BindingResult result,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        if (result.hasErrors()) return "extras/formulario";
        extraService.guardar(extra);
        return "redirect:/extras";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        extraService.eliminar(id);
        return "redirect:/extras";
    }
}

