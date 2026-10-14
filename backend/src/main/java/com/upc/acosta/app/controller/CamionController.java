package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.CamionDTORequest;
import com.upc.acosta.app.dto.CamionDTOResponse;
import com.upc.acosta.app.dto.DocumentoCamionDTORequest;
import com.upc.acosta.app.dto.DocumentoCamionDTOResponse;
import com.upc.acosta.app.service.IDocumentoCamionService;
import jakarta.validation.Valid;
import com.upc.acosta.app.service.ICamionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/camion")
public class CamionController {

    private final ICamionService camionService;
    private final IDocumentoCamionService documentoCamionService;

    public CamionController(
            ICamionService camionService,
            IDocumentoCamionService documentoCamionService
    ) {
        this.camionService = camionService;
        this.documentoCamionService = documentoCamionService;
    }

    @GetMapping
    public ResponseEntity<List<CamionDTOResponse>> listar() {
        return ResponseEntity.ok(camionService.listar());
    }

    @PostMapping
    public ResponseEntity<CamionDTOResponse> registrar(@Valid @RequestBody CamionDTORequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(camionService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamionDTOResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CamionDTORequest request
    ) {
        return ResponseEntity.ok(camionService.actualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamionDTOResponse> buscarXId(@PathVariable Long id) {
        return ResponseEntity.ok(camionService.buscarXId(id));
    }

    @GetMapping("/buscar/{texto}")
    public ResponseEntity<List<CamionDTOResponse>> buscarPorPlaca(@PathVariable String texto) {
        return ResponseEntity.ok(camionService.buscarPorPlaca(texto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        camionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/documentos")
    public ResponseEntity<List<DocumentoCamionDTOResponse>> listarDocumentos(@PathVariable Long id) {
        return ResponseEntity.ok(documentoCamionService.listar(id));
    }

    @PostMapping(value = "/{id}/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoCamionDTOResponse> registrarDocumento(
            @PathVariable Long id,
            @Valid @RequestPart("datos") DocumentoCamionDTORequest datos,
            @RequestPart("archivo") MultipartFile archivo
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentoCamionService.registrar(id, datos, archivo));
    }

    @PutMapping(
            value = "/{id}/documentos/{idDocumento}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentoCamionDTOResponse> actualizarDocumento(
            @PathVariable Long id,
            @PathVariable Long idDocumento,
            @Valid @RequestPart("datos") DocumentoCamionDTORequest datos,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo
    ) {
        return ResponseEntity.ok(documentoCamionService.actualizar(id, idDocumento, datos, archivo));
    }

    @DeleteMapping("/{id}/documentos/{idDocumento}")
    public ResponseEntity<Void> eliminarDocumento(
            @PathVariable Long id,
            @PathVariable Long idDocumento
    ) {
        documentoCamionService.eliminar(id, idDocumento);
        return ResponseEntity.noContent().build();
    }
}
