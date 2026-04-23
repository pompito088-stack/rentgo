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
import java.time.LocalDateTime;
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
    private final MantenimientoService mantenimientoService;
    private final DevolucionService devolucionService;

    public ReservaController(ReservaService reservaService,
                             VehiculoService vehiculoService,
                             SucursalService sucursalService,
                             UsuarioService usuarioService,
                             PagoService pagoService,
                             ExtraService extraService,
                             MantenimientoService mantenimientoService,
                             DevolucionService devolucionService) {
        this.reservaService = reservaService;
        this.vehiculoService = vehiculoService;
        this.sucursalService = sucursalService;
        this.usuarioService = usuarioService;
        this.pagoService = pagoService;
        this.extraService = extraService;
        this.mantenimientoService = mantenimientoService;
        this.devolucionService = devolucionService;
    }

    private boolean noEstaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null;
    }

    /** GET /reservas */
    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEstaLogueado(session)) return "redirect:/login";
        // Auto-actualizar estados (en_proceso / finalizada) en tiempo real segun fecha+hora
        reservaService.actualizarEstadosAutomaticamente();
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

        // Asignar horas de recogida y devolucion (las necesitamos antes de validar el mismo dia)
        if (horaInicioStr != null && !horaInicioStr.isBlank()) {
            reserva.setHoraInicio(LocalTime.parse(horaInicioStr));
        }
        if (horaFinStr != null && !horaFinStr.isBlank()) {
            reserva.setHoraFin(LocalTime.parse(horaFinStr));
        }

        // Si la reserva es del MISMO dia, la hora fin debe ser posterior a la hora inicio
        if (reserva.getFechaInicio() != null && reserva.getFechaFin() != null
                && reserva.getFechaInicio().isEqual(reserva.getFechaFin())
                && reserva.getHoraInicio() != null && reserva.getHoraFin() != null
                && !reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            result.rejectValue("fechaFin", "error.reserva",
                    "Para reservas del mismo dia la hora de devolucion debe ser posterior a la de recogida.");
            cargarDatosFormulario(model, session);
            return "reservas/formulario";
        }

        // No permitir reservar en el pasado: fecha+hora de inicio debe ser >= ahora
        if (reserva.getFechaInicio() != null && reserva.getHoraInicio() != null) {
            java.time.LocalDateTime inicio = java.time.LocalDateTime.of(reserva.getFechaInicio(), reserva.getHoraInicio());
            if (inicio.isBefore(java.time.LocalDateTime.now())) {
                result.rejectValue("fechaInicio", "error.reserva",
                        "No puedes reservar en el pasado. La fecha y hora de recogida deben ser futuras.");
                cargarDatosFormulario(model, session);
                return "reservas/formulario";
            }
        }

        // Validar solapamiento (reservas)
        if (reserva.getVehiculo() != null && reserva.getFechaInicio() != null && reserva.getFechaFin() != null) {
            boolean solapada = reserva.getId() != null
                    ? reservaService.existeSolapamiento(reserva.getVehiculo().getId(), reserva.getFechaInicio(), reserva.getFechaFin(), reserva.getId())
                    : reservaService.existeSolapamiento(reserva.getVehiculo().getId(), reserva.getFechaInicio(), reserva.getFechaFin());
            if (solapada) {
                result.rejectValue("vehiculo", "error.reserva", "El vehiculo ya tiene una reserva en esas fechas.");
                cargarDatosFormulario(model, session);
                return "reservas/formulario";
            }
            // Validar solapamiento con mantenimientos programados
            if (mantenimientoService.existeSolapamiento(reserva.getVehiculo().getId(),
                    reserva.getFechaInicio(), reserva.getFechaFin())) {
                result.rejectValue("vehiculo", "error.reserva",
                        "El vehiculo esta en mantenimiento durante esas fechas.");
                cargarDatosFormulario(model, session);
                return "reservas/formulario";
            }
        }

        // Resolver entidades transitorias desde la BD
        if (reserva.getVehiculo() != null && reserva.getVehiculo().getId() != null) {
            var vehiculoOpt = vehiculoService.buscarPorId(reserva.getVehiculo().getId());
            if (vehiculoOpt.isPresent()) {
                var vehiculoBD = vehiculoOpt.get();
                reserva.setVehiculo(vehiculoBD);
                // Sucursal de recogida = la sucursal actual del vehiculo
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

        // Calcular precio total: dias * precio_dia + extras * dias (minimo 1 dia)
        if (reserva.getVehiculo() != null && reserva.getVehiculo().getPrecioDia() != null
                && reserva.getFechaInicio() != null && reserva.getFechaFin() != null) {
            long dias = java.time.temporal.ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
            if (dias < 1) dias = 1; // mismo dia se cobra como 1 dia completo
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

        // Mantener estados al dia antes de evaluar la cancelacion
        reservaService.actualizarEstadosAutomaticamente();

        var reservaOpt = reservaService.buscarPorId(id);
        if (reservaOpt.isEmpty()) return "redirect:/reservas";

        Reserva reserva = reservaOpt.get();
        // Solo el propietario puede cancelar, y solo si la reserva NO ha empezado aun
        // (estado "confirmada" o "pendiente"). Una vez en proceso o finalizada no se puede cancelar.
        if (!reserva.getUsuario().getId().equals(logueado.getId())) return "redirect:/reservas";
        if (!("confirmada".equals(reserva.getEstado()) || "pendiente".equals(reserva.getEstado()))
                || reserva.isYaIniciada()) {
            return "redirect:/reservas";
        }

        // Calcular devolucion segun horas reales hasta el inicio:
        //   ≥ 24 h  → 100 %
        //   < 24 h  → 50 %
        BigDecimal devolucion = reserva.getPrecioTotal();
        String politica = "Cancelacion con 24 horas o mas de antelacion: devolucion del 100% del importe del alquiler.";
        if (reserva.getFechaInicio() != null) {
            LocalTime hi = reserva.getHoraInicio() != null ? reserva.getHoraInicio() : LocalTime.of(9, 0);
            LocalDateTime inicio = LocalDateTime.of(reserva.getFechaInicio(), hi);
            long horasHastaInicio = java.time.temporal.ChronoUnit.HOURS.between(LocalDateTime.now(), inicio);
            if (horasHastaInicio < 24) {
                devolucion = reserva.getPrecioTotal().divide(BigDecimal.valueOf(2), 2, java.math.RoundingMode.HALF_UP);
                politica = "Cancelacion con menos de 24 horas de antelacion: devolucion del 50% del importe del alquiler.";
            }
        }

        // La fianza siempre se devuelve integra
        BigDecimal fianzaDevolucion = BigDecimal.ZERO;
        var pagoOpt = pagoService.buscarPorReserva(reserva.getId());
        if (pagoOpt.isPresent() && pagoOpt.get().getFianza() != null) {
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

        // Asegurar estados al dia (por si la reserva ya entro en proceso entre tanto)
        reservaService.actualizarEstadosAutomaticamente();

        var reservaOpt = reservaService.buscarPorId(id);
        if (reservaOpt.isEmpty()) return "redirect:/reservas";

        Reserva reserva = reservaOpt.get();
        if (!reserva.getUsuario().getId().equals(logueado.getId())) return "redirect:/reservas";
        // Solo se permite cancelar si la reserva NO ha empezado aun
        if (!("confirmada".equals(reserva.getEstado()) || "pendiente".equals(reserva.getEstado()))
                || reserva.isYaIniciada()) {
            return "redirect:/reservas";
        }

        // Cancelar la reserva
        reserva.setEstado("cancelada");
        reservaService.guardar(reserva);

        // Calcular importe a devolver (mismo criterio que el GET):
        //   >= 24 h hasta el inicio  -> 100 % alquiler
        //   <  24 h                   -> 50 %  alquiler
        // La fianza siempre se devuelve integra.
        BigDecimal devAlquiler = reserva.getPrecioTotal();
        String motivoPolitica = "Cancelacion con 24h o mas de antelacion (100% del alquiler)";
        if (reserva.getFechaInicio() != null) {
            LocalTime hi = reserva.getHoraInicio() != null ? reserva.getHoraInicio() : LocalTime.of(9, 0);
            LocalDateTime inicio = LocalDateTime.of(reserva.getFechaInicio(), hi);
            long horasHastaInicio = java.time.temporal.ChronoUnit.HOURS.between(LocalDateTime.now(), inicio);
            if (horasHastaInicio < 24) {
                devAlquiler = reserva.getPrecioTotal().divide(BigDecimal.valueOf(2), 2, java.math.RoundingMode.HALF_UP);
                motivoPolitica = "Cancelacion con menos de 24h de antelacion (50% del alquiler)";
            }
        }

        // Crear la Devolucion asociada al pago (si existe pago y aun no tiene devolucion)
        final BigDecimal devAlquilerFinal = devAlquiler;
        final String motivoFinal = motivoPolitica;
        pagoService.buscarPorReserva(id).ifPresent(pago -> {
            if (devolucionService.buscarPorPago(pago.getId()).isEmpty()) {
                BigDecimal fianza = pago.getFianza() != null ? pago.getFianza() : BigDecimal.ZERO;
                BigDecimal totalReembolso = devAlquilerFinal.add(fianza);

                com.ilerna.rentgo.model.Devolucion dev = new com.ilerna.rentgo.model.Devolucion();
                dev.setPago(pago);
                dev.setFechaDevolucion(LocalDate.now());
                dev.setImporteReembolsado(totalReembolso);
                dev.setMotivo(motivoFinal);
                devolucionService.guardar(dev); // tambien marca el pago como 'reembolsado'
            } else {
                // Por seguridad, si ya hay devolucion solo aseguramos el estado
                pago.setEstadoPago("reembolsado");
                pagoService.guardar(pago);
            }
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

