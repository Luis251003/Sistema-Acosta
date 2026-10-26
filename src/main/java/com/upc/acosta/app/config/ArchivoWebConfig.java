package com.upc.acosta.app.config;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class ArchivoWebConfig implements WebMvcConfigurer {

    private final Path directorioBase;

    public ArchivoWebConfig(@Value("${app.archivos.directorio:uploads}") String directorio) {
        this.directorioBase = Path.of(directorio)
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/conductores/**")
                .addResourceLocations(location("conductores"));
        registry.addResourceHandler("/uploads/documentos/**")
                .addResourceLocations(location("documentos"));
    }

    private String location(String subdirectorio) {
        String location = directorioBase.resolve(subdirectorio).toUri().toString();
        return location.endsWith("/") ? location : location + "/";
    }
}
