package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.ResultadoActualizacion;
import com.upc.acosta.app.service.interfaces.IDocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/documentos")
@RequiredArgsConstructor
public class TareasInternasController {

    private final IDocumentoService documentoService;

    @PostMapping("/actualizar-estados")
    public ResponseEntity<ResultadoActualizacion> actualizarEstados() {
        return ResponseEntity.ok(documentoService.actualizarTodosLosEstados());
    }
}