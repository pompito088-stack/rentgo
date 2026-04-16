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
 * sin necesidad de que estén dentro del classpath.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Convierte la ruta relativa a ruta absoluta del sistema de archivos
        Path uploadDir = Paths.get(uploadPath).toAbsolutePath();

        // Mapea las peticiones /img/vehiculos/** a la carpeta de uploads en disco
        registry.addResourceHandler("/img/vehiculos/**")
                .addResourceLocations("file:" + uploadDir.toString() + "/");
    }
}

