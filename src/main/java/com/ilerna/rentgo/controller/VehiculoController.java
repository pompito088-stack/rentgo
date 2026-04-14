package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.model.Vehiculo;
import com.ilerna.rentgo.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador CRUD para Vehiculo.
 * Admin: gestion completa. Cliente: solo consulta.
 */
@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final ModeloService modeloService;
    private final CategoriaVehiculoService categoriaService;
    private final SucursalService sucursalService;
    private final ExtraService extraService;

    public VehiculoController(VehiculoService vehiculoService,
                              ModeloService modeloService,
                              CategoriaVehiculoService categoriaService,
                              SucursalService sucursalService,
                              ExtraService extraService) {
        this.vehiculoService = vehiculoService;
        this.modeloService = modeloService;
        this.categoriaService = categoriaService;
        this.sucursalService = sucursalService;
        this.extraService = extraService;
    }

    private boolean noEstaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    /** GET /vehiculos - Lista vehiculos (accesible por admin y cliente). */
    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("esAdmin", logueado.isAdmin());
        return "vehiculos/lista";
    }

    /** GET /vehiculos/detalle/{id} - Detalle de un vehiculo. */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Optional<Vehiculo> vehiculo = vehiculoService.buscarPorId(id);
        if (vehiculo.isPresent()) {
            model.addAttribute("vehiculo", vehiculo.get());
            return "vehiculos/detalle";
        }
        return "redirect:/vehiculos";
    }

    /** GET /vehiculos/nuevo - Formulario para crear vehiculo (solo admin). */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("vehiculo", new Vehiculo());
        cargarDatosFormulario(model);
        return "vehiculos/formulario";
    }

    /** GET /vehiculos/editar/{id} - Formulario para editar vehiculo (solo admin). */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Vehiculo> vehiculo = vehiculoService.buscarPorId(id);
        if (vehiculo.isPresent()) {
            model.addAttribute("vehiculo", vehiculo.get());
            cargarDatosFormulario(model);
            return "vehiculos/formulario";
        }
        return "redirect:/vehiculos";
    }

    /** POST /vehiculos/guardar - Guarda o actualiza un vehiculo (solo admin). */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Vehiculo vehiculo,
                          BindingResult result,
                          @RequestParam(value = "foto", required = false) MultipartFile foto,
                          Model model,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";

        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "vehiculos/formulario";
        }

        // Comprobar matricula duplicada
        Optional<Vehiculo> existente = vehiculoService.buscarPorMatricula(vehiculo.getMatricula());
        if (existente.isPresent() && !existente.get().getId().equals(vehiculo.getId())) {
            result.rejectValue("matricula", "error.vehiculo", "Esta matricula ya esta registrada.");
            cargarDatosFormulario(model);
            return "vehiculos/formulario";
        }

        // Subir foto si se proporciona
        if (foto != null && !foto.isEmpty()) {
            try {
                String nombreArchivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();
                Path rutaDirectorio = Paths.get("src/main/resources/static/img/vehiculos");
                Files.createDirectories(rutaDirectorio);
                Path rutaArchivo = rutaDirectorio.resolve(nombreArchivo);
                Files.write(rutaArchivo, foto.getBytes());
                vehiculo.setRutaFoto("/img/vehiculos/" + nombreArchivo);
            } catch (IOException e) {
                // Si falla la subida, continua sin foto
            }
        } else if (vehiculo.getId() != null) {
            // En edicion, mantener foto anterior si no se sube nueva
            vehiculoService.buscarPorId(vehiculo.getId())
                    .ifPresent(v -> vehiculo.setRutaFoto(v.getRutaFoto()));
        }

        vehiculoService.guardar(vehiculo);
        return "redirect:/vehiculos";
    }

    /** GET /vehiculos/eliminar/{id} - Elimina un vehiculo (solo admin). */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        vehiculoService.eliminar(id);
        return "redirect:/vehiculos";
    }

    /** Carga los datos necesarios para los selects del formulario. */
    private void cargarDatosFormulario(Model model) {
        model.addAttribute("modelos", modeloService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("sucursales", sucursalService.listarTodas());
        model.addAttribute("extras", extraService.listarTodos());
    }
}

