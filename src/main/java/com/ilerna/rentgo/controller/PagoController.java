package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Pago;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.service.PagoService;
import com.ilerna.rentgo.service.ReservaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador CRUD para Pago.
 * Solo accesible por administradores.
 */
@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final ReservaService reservaService;

    public PagoController(PagoService pagoService, ReservaService reservaService) {
        this.pagoService = pagoService;
        this.reservaService = reservaService;
    }

    private boolean noEsAdmin(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        return logueado == null || !logueado.isAdmin();
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("pagos", pagoService.listarTodos());
        return "pagos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        model.addAttribute("pago", new Pago());
        model.addAttribute("reservas", reservaService.listarTodas());
        model.addAttribute("metodosPago", new String[]{"tarjeta_credito", "tarjeta_debito", "paypal"});
        return "pagos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        Optional<Pago> pago = pagoService.buscarPorId(id);
        if (pago.isPresent()) {
            model.addAttribute("pago", pago.get());
            model.addAttribute("reservas", reservaService.listarTodas());
            model.addAttribute("metodosPago", new String[]{"tarjeta_credito", "tarjeta_debito", "paypal"});
            return "pagos/formulario";
        }
        return "redirect:/pagos";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Pago pago,
                          BindingResult result,
                          Model model,
                          HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        pago.setEstadoPago("realizado");
        if (result.hasErrors()) {
            model.addAttribute("reservas", reservaService.listarTodas());
            model.addAttribute("metodosPago", new String[]{"tarjeta_credito", "tarjeta_debito", "paypal"});
            return "pagos/formulario";
        }
        pagoService.guardar(pago);
        return "redirect:/pagos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        if (noEsAdmin(session)) return "redirect:/login";
        pagoService.eliminar(id);
        return "redirect:/pagos";
    }
}

