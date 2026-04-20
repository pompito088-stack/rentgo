package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.model.Pago;
import com.ilerna.rentgo.model.Reserva;
import com.ilerna.rentgo.model.Usuario;
import com.ilerna.rentgo.model.Extra;
import com.ilerna.rentgo.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controlador CRUD para Reserva.
 * Admin: ve todas las reservas. Cliente: solo las suyas.
 * Al crear una reserva, se genera automaticamente un Pago asociado.
 */
@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final VehiculoService vehiculoService;
    private final SucursalService sucursalService;
    private final UsuarioService usuarioService;
    private final PagoService pagoService;
    private final ExtraService extraService;

    public ReservaController(ReservaService reservaService,
                             VehiculoService vehiculoService,
                             SucursalService sucursalService,
                             UsuarioService usuarioService,
                             PagoService pagoService,
                             ExtraService extraService) {
        this.reservaService = reservaService;
        this.vehiculoService = vehiculoService;
        this.sucursalService = sucursalService;
        this.usuarioService = usuarioService;
        this.pagoService = pagoService;
        this.extraService = extraService;
    }

    private boolean noEstaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null;
    }

    /** GET /reservas */
    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (logueado.isAdmin()) {
            model.addAttribute("reservas", reservaService.listarTodas());
        } else {
            model.addAttribute("reservas", reservaService.listarPorUsuario(logueado.getId()));
        }
        model.addAttribute("esAdmin", logueado.isAdmin());
        return "reservas/lista";
    }

    /** GET /reservas/nuevo */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(@RequestParam(value = "vehiculoId", required = false) Integer vehiculoId,
                                         Model model, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (logueado.isAdmin()) return "redirect:/reservas";

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.now());
        reserva.setEstado("pendiente");

        // Preseleccionar vehiculo si viene del detalle
        if (vehiculoId != null) {
            vehiculoService.buscarPorId(vehiculoId).ifPresent(v -> {
                com.ilerna.rentgo.model.Vehiculo vRef = new com.ilerna.rentgo.model.Vehiculo();
                vRef.setId(v.getId());
                reserva.setVehiculo(vRef);
            });
        }

        model.addAttribute("reserva", reserva);
        model.addAttribute("vehiculoPreseleccionado", vehiculoId);
        cargarDatosFormulario(model, session);
        return "reservas/formulario";
    }

    /** GET /reservas/editar/{id} - Bloqueado. */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar() {
        return "redirect:/reservas";
    }

    /**
     * POST /reservas/guardar
     * Guarda la reserva y crea automaticamente el pago asociado.
     * El metodo de pago lo elige el cliente en el formulario.
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Reserva reserva,
                          BindingResult result,
                          @RequestParam(value = "metodoPago", required = false) String metodoPago,
                          @RequestParam(value = "extrasIds", required = false) List<Integer> extrasIds,
                          @RequestParam(value = "horaInicio", required = false) String horaInicioStr,
                          @RequestParam(value = "horaFin", required = false) String horaFinStr,
                          Model model,
                          HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (result.hasErrors()) {
            cargarDatosFormulario(model, session);
            return "reservas/formulario";
        }

        // Validar fechas
        if (reserva.getFechaFin() != null && reserva.getFechaInicio() != null
                && reserva.getFechaFin().isBefore(reserva.getFechaInicio())) {
            result.rejectValue("fechaFin", "error.reserva", "La fecha de fin debe ser posterior a la de inicio.");
            cargarDatosFormulario(model, session);
            return "reservas/formulario";
        }

        // Validar solapamiento
        if (reserva.getVehiculo() != null && reserva.getFechaInicio() != null && reserva.getFechaFin() != null) {
            boolean solapada = reserva.getId() != null
                    ? reservaService.existeSolapamiento(reserva.getVehiculo().getId(), reserva.getFechaInicio(), reserva.getFechaFin(), reserva.getId())
                    : reservaService.existeSolapamiento(reserva.getVehiculo().getId(), reserva.getFechaInicio(), reserva.getFechaFin());
            if (solapada) {
                result.rejectValue("vehiculo", "error.reserva", "El vehiculo ya tiene una reserva en esas fechas.");
                cargarDatosFormulario(model, session);
                return "reservas/formulario";
            }
        }

        // Asignar horas de recogida y devolucion
        if (horaInicioStr != null && !horaInicioStr.isBlank()) {
            reserva.setHoraInicio(LocalTime.parse(horaInicioStr));
        }
        if (horaFinStr != null && !horaFinStr.isBlank()) {
            reserva.setHoraFin(LocalTime.parse(horaFinStr));
        }

        // Asignar usuario y estado si es cliente
        if (!logueado.isAdmin()) {
            reserva.setUsuario(logueado);
            reserva.setEstado("confirmada");
        }

        // Calcular precio total: dias * precio_dia + extras * dias
        if (reserva.getVehiculo() != null && reserva.getFechaInicio() != null && reserva.getFechaFin() != null) {
            long dias = java.time.temporal.ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
            if (dias > 0) {
                // Precio vehiculo
                final long[] diasFinal = {dias};
                vehiculoService.buscarPorId(reserva.getVehiculo().getId()).ifPresent(v -> {
                    BigDecimal total = v.getPrecioDia().multiply(BigDecimal.valueOf(diasFinal[0]));
                    // Sumar extras si se han seleccionado
                    if (extrasIds != null && !extrasIds.isEmpty()) {
                        for (Integer extraId : extrasIds) {
                            extraService.buscarPorId(extraId).ifPresent(e ->
                                total.add(e.getPrecioDia().multiply(BigDecimal.valueOf(diasFinal[0])))
                            );
                        }
                        // Recalcular correctamente (lambda no puede modificar variable)
                        BigDecimal totalExtras = BigDecimal.ZERO;
                        for (Integer extraId : extrasIds) {
                            var extraOpt = extraService.buscarPorId(extraId);
                            if (extraOpt.isPresent()) {
                                totalExtras = totalExtras.add(extraOpt.get().getPrecioDia().multiply(BigDecimal.valueOf(diasFinal[0])));
                            }
                        }
                        reserva.setPrecioTotal(v.getPrecioDia().multiply(BigDecimal.valueOf(diasFinal[0])).add(totalExtras));
                    } else {
                        reserva.setPrecioTotal(total);
                    }
                });
            }
        }

        // Asignar extras seleccionados
        if (extrasIds != null && !extrasIds.isEmpty()) {
            Set<Extra> extrasSeleccionados = new HashSet<>();
            for (Integer extraId : extrasIds) {
                extraService.buscarPorId(extraId).ifPresent(extrasSeleccionados::add);
            }
            reserva.setExtras(extrasSeleccionados);
        }

        // Establecer fecha de reserva si es nueva
        if (reserva.getId() == null) {
            reserva.setFechaReserva(LocalDate.now());
        }

        // Capturar ANTES de guardar para evitar que JPA asigne el id
        boolean esNueva = reserva.getId() == null;
        Reserva reservaGuardada = reservaService.guardar(reserva);

        // Crear pago automatico si es cliente y es una reserva nueva
        if (!logueado.isAdmin() && esNueva && reservaGuardada != null) {
            Pago pago = new Pago();
            pago.setFechaPago(LocalDate.now());
            pago.setImporte(reservaGuardada.getPrecioTotal());
            // Fianza del vehiculo
            if (reservaGuardada.getVehiculo() != null) {
                vehiculoService.buscarPorId(reservaGuardada.getVehiculo().getId())
                    .ifPresent(v -> pago.setFianza(v.getFianza() != null ? v.getFianza() : java.math.BigDecimal.ZERO));
            }
            pago.setMetodoPago(metodoPago != null && !metodoPago.isBlank() ? metodoPago : "tarjeta_credito");
            pago.setEstadoPago("realizado");
            pago.setReserva(reservaGuardada);
            pagoService.guardar(pago);
        }

        return "redirect:/reservas";
    }

    /** GET /reservas/eliminar/{id} - Bloqueado. */
    @GetMapping("/eliminar/{id}")
    public String eliminar() {
        return "redirect:/reservas";
    }

    private void cargarDatosFormulario(Model model, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("sucursales", sucursalService.listarTodas());
        model.addAttribute("extras", extraService.listarTodos());
        model.addAttribute("esAdmin", logueado != null && logueado.isAdmin());
        model.addAttribute("metodosPago", new String[]{"tarjeta_credito", "tarjeta_debito", "paypal"});
        if (logueado != null && logueado.isAdmin()) {
            model.addAttribute("usuarios", usuarioService.listarTodos());
        }
        model.addAttribute("estados", new String[]{"pendiente", "confirmada", "cancelada", "finalizada"});
    }
}

