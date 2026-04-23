package com.ilerna.rentgo.controller;

import com.ilerna.rentgo.service.SucursalService;
import com.ilerna.rentgo.service.VehiculoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

/**
 * SEO 1.12 — Sitemap XML dinamico.
 * Se autodescubre desde robots.txt y enumera URLs publicas:
 *  - Pagina de inicio
 *  - Listado de vehiculos + ficha de cada vehiculo
 *  - Listado de sucursales
 *  - Listado de extras
 * Se actualiza automaticamente al anadir/eliminar registros en BD.
 */
@Controller
public class SitemapController {

    private final VehiculoService vehiculoService;
    private final SucursalService sucursalService;

    public SitemapController(VehiculoService vehiculoService, SucursalService sucursalService) {
        this.vehiculoService = vehiculoService;
        this.sucursalService = sucursalService;
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap(HttpServletRequest request) {
        // Base URL detectada en runtime (funciona en local y en produccion)
        String base = request.getScheme() + "://" + request.getServerName()
                + (isStandardPort(request) ? "" : ":" + request.getServerPort());
        String today = LocalDate.now().toString();

        StringBuilder xml = new StringBuilder(2048);
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Paginas estaticas publicas
        addUrl(xml, base + "/",            today, "daily",   "1.0");
        addUrl(xml, base + "/vehiculos",   today, "daily",   "0.9");
        addUrl(xml, base + "/sucursales",  today, "weekly",  "0.7");
        addUrl(xml, base + "/extras",      today, "monthly", "0.5");

        // Ficha de cada vehiculo
        try {
            vehiculoService.listarTodos().forEach(v ->
                    addUrl(xml, base + "/vehiculos/detalle/" + v.getId(), today, "weekly", "0.8")
            );
        } catch (Exception ignored) { /* defensivo: no romper sitemap si falla la BD */ }

        // Detalle de sucursal (si existe la ruta en el futuro). Por ahora solo el listado.
        try {
            List<?> sucursales = sucursalService.listarTodas();
            if (sucursales != null) {
                // Placeholder: cuando se anada /sucursales/detalle/{id} descomentar
                // sucursales.forEach(s -> addUrl(xml, base + "/sucursales/detalle/" + ((com.ilerna.rentgo.model.Sucursal)s).getId(), today, "monthly", "0.6"));
            }
        } catch (Exception ignored) {}

        xml.append("</urlset>\n");
        return xml.toString();
    }

    private void addUrl(StringBuilder xml, String loc, String lastmod, String changefreq, String priority) {
        xml.append("  <url>\n")
           .append("    <loc>").append(escapeXml(loc)).append("</loc>\n")
           .append("    <lastmod>").append(lastmod).append("</lastmod>\n")
           .append("    <changefreq>").append(changefreq).append("</changefreq>\n")
           .append("    <priority>").append(priority).append("</priority>\n")
           .append("  </url>\n");
    }

    private boolean isStandardPort(HttpServletRequest req) {
        int p = req.getServerPort();
        return ("http".equals(req.getScheme()) && p == 80)
            || ("https".equals(req.getScheme()) && p == 443);
    }

    private String escapeXml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}

