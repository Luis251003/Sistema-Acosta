package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.PuntoOrigenResolverDTORequest;
import com.upc.acosta.app.dto.ResultadoOrigen;
import com.upc.acosta.app.service.interfaces.IPuntoOrigenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/punto-origen")
public class PuntoOrigenController {

    private final IPuntoOrigenService puntoOrigenService;

    public PuntoOrigenController(IPuntoOrigenService puntoOrigenService) {
        this.puntoOrigenService = puntoOrigenService;
    }

    @PostMapping("/resolver")
    public ResponseEntity<?> resolver(
            @Valid @RequestBody PuntoOrigenResolverDTORequest request
    ) {
        ResultadoOrigen resultado = puntoOrigenService.resolver(request.direccion());
        return ResponseEntity.ok(resultado);
    }
}
