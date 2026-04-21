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
        // Auto-finalizar reservas expiradas en tiempo real
        reservaService.autoFinalizarExpiradas();
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

        // Resolver entidades transitorias desde la BD
        if (reserva.getVehiculo() != null && reserva.getVehiculo().getId() != null) {
            var vehiculoOpt = vehiculoService.buscarPorId(reserva.getVehiculo().getId());
            if (vehiculoOpt.isPresent()) {
                var vehiculoBD = vehiculoOpt.get();
                reserva.setVehiculo(vehiculoBD);
                // Sucursal de recogida = la sucursal actual del vehículo
                reserva.setSucursalRecogida(vehiculoBD.getSucursal());
            }
        }
        if (reserva.getSucursalDevolucion() != null && reserva.getSucursalDevolucion().getId() != null) {
            sucursalService.buscarPorId(reserva.getSucursalDevolucion().getId()).ifPresent(reserva::setSucursalDevolucion);
        }

        // Asignar usuario y estado si es cliente
        if (!logueado.isAdmin()) {
            reserva.setUsuario(logueado);
            reserva.setEstado("confirmada");
        }

        // Calcular precio total: dias * precio_dia + extras * dias
        if (reserva.getVehiculo() != null && reserva.getVehiculo().getPrecioDia() != null
                && reserva.getFechaInicio() != null && reserva.getFechaFin() != null) {
            long dias = java.time.temporal.ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
            if (dias > 0) {
                BigDecimal totalVehiculo = reserva.getVehiculo().getPrecioDia().multiply(BigDecimal.valueOf(dias));
                BigDecimal totalExtras = BigDecimal.ZERO;
                if (extrasIds != null && !extrasIds.isEmpty()) {
                    for (Integer extraId : extrasIds) {
                        var extraOpt = extraService.buscarPorId(extraId);
                        if (extraOpt.isPresent()) {
                            totalExtras = totalExtras.add(extraOpt.get().getPrecioDia().multiply(BigDecimal.valueOf(dias)));
                        }
                    }
                }
                reserva.setPrecioTotal(totalVehiculo.add(totalExtras));
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

        // Actualizar la sucursal del vehiculo a la de devolucion
        // (la nueva ubicacion real del coche tras la devolucion)
        if (reservaGuardada != null && reservaGuardada.getSucursalDevolucion() != null
                && reservaGuardada.getVehiculo() != null) {
            vehiculoService.buscarPorId(reservaGuardada.getVehiculo().getId()).ifPresent(v -> {
                v.setSucursal(reservaGuardada.getSucursalDevolucion());
                vehiculoService.guardar(v);
            });
        }

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

    /**
     * GET /reservas/cancelar/{id}
     * Muestra la pagina de confirmacion de cancelacion con el resumen de la reserva
     * y el importe a devolver.
     */
    @GetMapping("/cancelar/{id}")
    public String mostrarCancelacion(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");

        var reservaOpt = reservaService.buscarPorId(id);
        if (reservaOpt.isEmpty()) return "redirect:/reservas";

        Reserva reserva = reservaOpt.get();
        // Solo el propietario puede cancelar, y solo si esta confirmada o en_proceso
        if (!reserva.getUsuario().getId().equals(logueado.getId())) return "redirect:/reservas";
        if (!"confirmada".equals(reserva.getEstado()) && !"en_proceso".equals(reserva.getEstado())) {
            return "redirect:/reservas";
        }

        // Calcular devolucion: si faltan mas de 24h → 100%, sino 50%
        BigDecimal devolucion = reserva.getPrecioTotal();
        String politica = "Cancelación con más de 24 horas de antelación: devolución del 100%.";
        if (reserva.getFechaInicio() != null) {
            long diasHastaInicio = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), reserva.getFechaInicio());
            if (diasHastaInicio < 1) {
                devolucion = reserva.getPrecioTotal().divide(BigDecimal.valueOf(2), 2, java.math.RoundingMode.HALF_UP);
                politica = "Cancelación con menos de 24 horas de antelación: devolución del 50%.";
            }
        }

        // Fianza siempre se devuelve
        BigDecimal fianzaDevolucion = BigDecimal.ZERO;
        var pagoOpt = pagoService.buscarPorReserva(reserva.getId());
        if (pagoOpt.isPresent()) {
            fianzaDevolucion = pagoOpt.get().getFianza();
        }

        model.addAttribute("reserva", reserva);
        model.addAttribute("devolucion", devolucion);
        model.addAttribute("fianzaDevolucion", fianzaDevolucion);
        model.addAttribute("totalDevolucion", devolucion.add(fianzaDevolucion));
        model.addAttribute("politica", politica);
        return "reservas/cancelar";
    }

    /**
     * POST /reservas/cancelar/{id}
     * Confirma la cancelacion de la reserva: cambia estado a "cancelada"
     * y actualiza el pago a estado "reembolsado".
     */
    @PostMapping("/cancelar/{id}")
    public String confirmarCancelacion(@PathVariable Integer id, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");

        var reservaOpt = reservaService.buscarPorId(id);
        if (reservaOpt.isEmpty()) return "redirect:/reservas";

        Reserva reserva = reservaOpt.get();
        if (!reserva.getUsuario().getId().equals(logueado.getId())) return "redirect:/reservas";
        if (!"confirmada".equals(reserva.getEstado()) && !"en_proceso".equals(reserva.getEstado())) {
            return "redirect:/reservas";
        }

        // Cancelar la reserva
        reserva.setEstado("cancelada");
        reservaService.guardar(reserva);

        // Actualizar pago a reembolsado
        pagoService.buscarPorReserva(id).ifPresent(pago -> {
            pago.setEstadoPago("reembolsado");
            pagoService.guardar(pago);
        });

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
        model.addAttribute("estados", new String[]{"pendiente", "confirmada", "en_proceso", "cancelada", "finalizada"});
    }
}

