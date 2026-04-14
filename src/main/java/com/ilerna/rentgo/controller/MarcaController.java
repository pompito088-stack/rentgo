package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Marca;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.MarcaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador CRUD para Marca.
 * Solo accesible por administradores.
 */
@Controller
@RequestMapping("/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("marcas", marcaService.listarTodas());
        return "marcas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("marca", new Marca());
        return "marcas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Marca> marca = marcaService.buscarPorId(id);
        if (marca.isPresent()) {
            model.addAttribute("marca", marca.get());
            return "marcas/formulario";
        }
        return "redirect:/marcas";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Marca marca,
                          BindingResult result,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        if (result.hasErrors()) return "marcas/formulario";

        // Comprobar nombre duplicado
        Optional<Marca> existente = marcaService.buscarPorNombre(marca.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(marca.getId())) {
            result.rejectValue("nombre", "error.marca", "Esta marca ya existe.");
            return "marcas/formulario";
        }

        marcaService.guardar(marca);
        return "redirect:/marcas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        marcaService.eliminar(id);
        return "redirect:/marcas";
    }
}

