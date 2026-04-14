package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Modelo;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.MarcaService;
import com.ilerna.rentgo.service.ModeloService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador CRUD para Modelo.
 * Solo accesible por administradores.
 */
@Controller
@RequestMapping("/modelos")
public class ModeloController {

    private final ModeloService modeloService;
    private final MarcaService marcaService;

    public ModeloController(ModeloService modeloService, MarcaService marcaService) {
        this.modeloService = modeloService;
        this.marcaService = marcaService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("modelos", modeloService.listarTodos());
        return "modelos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("modelo", new Modelo());
        model.addAttribute("marcas", marcaService.listarTodas());
        return "modelos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Modelo> modelo = modeloService.buscarPorId(id);
        if (modelo.isPresent()) {
            model.addAttribute("modelo", modelo.get());
            model.addAttribute("marcas", marcaService.listarTodas());
            return "modelos/formulario";
        }
        return "redirect:/modelos";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Modelo modelo,
                          BindingResult result,
                          Model model,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        if (result.hasErrors()) {
            model.addAttribute("marcas", marcaService.listarTodas());
            return "modelos/formulario";
        }
        modeloService.guardar(modelo);
        return "redirect:/modelos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        modeloService.eliminar(id);
        return "redirect:/modelos";
    }
}

