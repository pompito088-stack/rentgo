package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.CategoriaVehiculo;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.CategoriaVehiculoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador CRUD para CategoriaVehiculo.
 * Solo accesible por administradores.
 */
@Controller
@RequestMapping("/categorias")
public class CategoriaVehiculoController {

    private final CategoriaVehiculoService categoriaService;

    public CategoriaVehiculoController(CategoriaVehiculoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categorias/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("categoria", new CategoriaVehiculo());
        return "categorias/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<CategoriaVehiculo> categoria = categoriaService.buscarPorId(id);
        if (categoria.isPresent()) {
            model.addAttribute("categoria", categoria.get());
            return "categorias/formulario";
        }
        return "redirect:/categorias";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") CategoriaVehiculo categoria,
                          BindingResult result,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        if (result.hasErrors()) return "categorias/formulario";
        categoriaService.guardar(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        categoriaService.eliminar(id);
        return "redirect:/categorias";
    }
}

