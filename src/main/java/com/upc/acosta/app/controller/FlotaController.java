package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.FlotaDTORequest;
import com.upc.acosta.app.service.interfaces.IFlotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flota")
@RequiredArgsConstructor
public class FlotaController {

    // DECLARAMOS LOS SERVICIOS
    private final IFlotaService flotaService;

    @PostMapping
    public ResponseEntity<?> registrarFlota(@Valid @RequestBody FlotaDTORequest flota){
        return ResponseEntity.status(HttpStatus.CREATED).body(flotaService.registrar(flota));
    }

    @PutMapping
    public ResponseEntity<?> actualizarFlota(@Valid @RequestBody FlotaDTORequest flota){
        return ResponseEntity.status(HttpStatus.CREATED).body(flotaService.actualizar(flota));
    }
}
