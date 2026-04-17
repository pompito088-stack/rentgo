package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.model.Vehiculo;
import com.ilerna.rentgo.service.*;
import com.ilerna.rentgo.service.MarcaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Controlador CRUD para Vehiculo.
 * Admin: gestion completa. Cliente: solo consulta.
 */
@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Value("${app.upload.path}")
    private String uploadPath;

    private final VehiculoService vehiculoService;
    private final ModeloService modeloService;
    private final CategoriaVehiculoService categoriaService;
    private final SucursalService sucursalService;
    private final ExtraService extraService;
    private final MarcaService marcaService;

    public VehiculoController(VehiculoService vehiculoService,
                              ModeloService modeloService,
                              CategoriaVehiculoService categoriaService,
                              SucursalService sucursalService,
                              ExtraService extraService,
                              MarcaService marcaService) {
        this.vehiculoService = vehiculoService;
        this.modeloService = modeloService;
        this.categoriaService = categoriaService;
        this.sucursalService = sucursalService;
        this.extraService = extraService;
        this.marcaService = marcaService;
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
                // Nombre semantico: espacios y caracteres raros -> guion bajo, todo en minusculas
                String nombreOriginal = foto.getOriginalFilename() != null
                        ? foto.getOriginalFilename() : "foto.jpg";
                String nombreArchivo = nombreOriginal
                        .toLowerCase()
                        .replaceAll("[^a-z0-9._-]", "_");

                // Si es una edicion y ya tenia foto, borrar la foto antigua del disco
                if (vehiculo.getId() != null) {
                    vehiculoService.buscarPorId(vehiculo.getId())
                            .ifPresent(v -> borrarFotoDelDisco(v.getRutaFoto()));
                }

                // Guardar la nueva foto en static/img/vehiculos
                Path rutaDirectorio = Paths.get(uploadPath).toAbsolutePath();
                Files.createDirectories(rutaDirectorio);
                Files.copy(foto.getInputStream(),
                        rutaDirectorio.resolve(nombreArchivo),
                        StandardCopyOption.REPLACE_EXISTING);
                vehiculo.setRutaFoto("/img/vehiculos/" + nombreArchivo);
            } catch (IOException e) {
                // Si falla la subida, continua sin foto
            }
        } else if (vehiculo.getId() != null) {
            // En edicion sin nueva foto, mantener la foto anterior
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

        // Borrar la foto del disco antes de eliminar el vehiculo de la BD
        vehiculoService.buscarPorId(id)
                .ifPresent(v -> borrarFotoDelDisco(v.getRutaFoto()));

        vehiculoService.eliminar(id);
        return "redirect:/vehiculos";
    }

    /**
     * Borra un archivo de foto del disco dado su ruta web (ej: /img/vehiculos/foto.jpg).
     * Extrae el nombre del archivo y lo elimina de la carpeta de uploads.
     */
    private void borrarFotoDelDisco(String rutaFoto) {
        if (rutaFoto == null || rutaFoto.isBlank()) return;
        try {
            // Extrae solo el nombre del archivo de la ruta web: /img/vehiculos/foto.jpg -> foto.jpg
            String nombreArchivo = Paths.get(rutaFoto).getFileName().toString();
            Path archivoEnDisco = Paths.get(uploadPath).toAbsolutePath().resolve(nombreArchivo);
            Files.deleteIfExists(archivoEnDisco);
        } catch (IOException e) {
            // Si falla el borrado, continua sin lanzar excepcion
        }
    }

    /** Carga los datos necesarios para los selects del formulario. */
    private void cargarDatosFormulario(Model model) {
        model.addAttribute("modelos", modeloService.listarTodos());
        model.addAttribute("marcas", marcaService.listarTodas());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("sucursales", sucursalService.listarTodas());
        model.addAttribute("extras", extraService.listarTodos());
        model.addAttribute("etiquetas", new String[]{"sin_etiqueta", "B", "C", "ECO", "CERO"});
        model.addAttribute("transmisiones", new String[]{"manual", "automatica"});
        model.addAttribute("combustiones", new String[]{
                "gasolina",
                "diesel",
                "hibrido_gasolina",
                "hibrido_diesel",
                "hibrido_enchufable_gasolina",
                "hibrido_enchufable_diesel",
                "electrico",
                "glp",
                "gnc",
                "hidrogeno"
        });
    }
}

