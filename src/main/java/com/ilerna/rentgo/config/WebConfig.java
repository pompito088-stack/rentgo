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
        // Convierte la ruta relativa a ruta absoluta del sistema de archivos
        Path uploadDir = Paths.get(uploadPath).toAbsolutePath();
        Path carnetDir = Paths.get(carnetUploadPath).toAbsolutePath();

        // Mapea /img/vehiculos/** a la carpeta static/img/vehiculos en disco
        // Asi Spring sirve los archivos subidos en runtime sin necesidad de reiniciar
        registry.addResourceHandler("/img/vehiculos/**")
                .addResourceLocations("file:" + uploadDir + "/");

        // Mapea /img/carnets/** a la carpeta static/img/carnets en disco
        // Permite ver las fotos de carnet recien subidas sin reiniciar
        registry.addResourceHandler("/img/carnets/**")
                .addResourceLocations("file:" + carnetDir + "/");

        // Mantener los recursos estaticos normales del classpath
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
