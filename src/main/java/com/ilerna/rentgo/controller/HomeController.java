package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la pagina de inicio.
 * Muestra dashboard distinto segun el rol (admin / cliente).
 */
@Controller
public class HomeController {

    private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;
    private final ReservaService reservaService;
    private final SucursalService sucursalService;

    public HomeController(UsuarioService usuarioService,
                          VehiculoService vehiculoService,
                          ReservaService reservaService,
                          SucursalService sucursalService) {
        this.usuarioService = usuarioService;
        this.vehiculoService = vehiculoService;
        this.reservaService = reservaService;
        this.sucursalService = sucursalService;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("esAdmin", usuario != null && usuario.isAdmin());

        if (usuario != null && usuario.isAdmin()) {
            // Estadisticas para el dashboard admin
            long totalUsuarios = usuarioService.listarTodos().size();
            long totalClientes = usuarioService.contarClientes();
            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("totalClientes", totalClientes);
            model.addAttribute("totalVehiculos", vehiculoService.listarTodos().size());
            model.addAttribute("totalReservas", reservaService.listarTodas().size());
            model.addAttribute("totalSucursales", sucursalService.listarTodas().size());
            // Auto-actualizar estados de reservas (en_proceso/finalizada) antes del dashboard
            reservaService.actualizarEstadosAutomaticamente();
            // Actividad reciente: ultimas 5 reservas
            model.addAttribute("ultimasReservas", reservaService.listarUltimas(5));
        }
        return "index";
    }
}
