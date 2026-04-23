package com.ilerna.rentgo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuracion de recursos estaticos.
 * Permite servir las fotos subidas por el admin desde el disco
 * sin necesidad de que esten dentro del classpath.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.path}")
    private String uploadPath;

    @Value("${app.upload.carnet.path}")
    private String carnetUploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Convierte la ruta relativa a ruta absoluta y la normaliza como URI file:///
        // (Path.toUri() genera el formato correcto en cualquier SO, incluido Windows)
        Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path carnetDir = Paths.get(carnetUploadPath).toAbsolutePath().normalize();

        // Construye la URL con formato file:/// correcto y asegura barra final
        String uploadLocation = uploadDir.toUri().toString();
        if (!uploadLocation.endsWith("/")) uploadLocation += "/";

        String carnetLocation = carnetDir.toUri().toString();
        if (!carnetLocation.endsWith("/")) carnetLocation += "/";

        // Mapea /img/vehiculos/** a la carpeta de subidas en disco (runtime)
        // con fallback al classpath para las imagenes seed incluidas en el JAR
        registry.addResourceHandler("/img/vehiculos/**")
                .addResourceLocations(uploadLocation, "classpath:/static/img/vehiculos/");

        // Mapea /img/carnets/** a la carpeta de subidas en disco (runtime)
        // con fallback al classpath para las imagenes seed incluidas en el JAR
        registry.addResourceHandler("/img/carnets/**")
                .addResourceLocations(carnetLocation, "classpath:/static/img/carnets/");

        // Nota: NO se re-registra "/**" aqui; Spring Boot ya gestiona
        // los recursos estaticos del classpath (/static, /public, etc.)
    }
}
