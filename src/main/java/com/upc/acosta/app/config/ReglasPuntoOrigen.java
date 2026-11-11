package com.upc.acosta.app.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class ReglasPuntoOrigen {

    private static final Map<Pattern,String> REGLAS = Map.of(
            Pattern.compile("contralmirante raygada.*\\b111\\b"), "APM Terminals Callao"
    );
    public Optional<String> buscarPuntoOrigen(String direccion) {
        return REGLAS.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(direccion).find())
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
