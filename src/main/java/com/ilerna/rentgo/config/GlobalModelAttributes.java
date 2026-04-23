package com.ilerna.rentgo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Inyecta variables globales del request en TODAS las vistas Thymeleaf.
 * Necesario porque Thymeleaf 3 + Spring 6 ya no exponen #request por defecto.
 *
 * Variables disponibles en cualquier plantilla:
 *   ${requestUri}  -> /vehiculos/detalle/5
 *   ${requestUrl}  -> http://localhost:8080/vehiculos/detalle/5
 *   ${baseUrl}     -> http://localhost:8080
 */
@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("requestUri")
    public String requestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("requestUrl")
    public String requestUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    @ModelAttribute("baseUrl")
    public String baseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        int port = request.getServerPort();
        boolean stdPort = ("http".equals(scheme) && port == 80)
                       || ("https".equals(scheme) && port == 443);
        return scheme + "://" + request.getServerName() + (stdPort ? "" : ":" + port);
    }
}

