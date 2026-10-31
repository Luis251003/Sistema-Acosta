package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.CamionDTORequest;
import com.upc.acosta.app.dto.CamionDTOResponse;
import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.dto.DocumentoDTOResponse;
import com.upc.acosta.app.entity.RolDocumento;
import com.upc.acosta.app.service.IDocumentoService;
import jakarta.validation.Valid;
import com.upc.acosta.app.service.ICamionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

@RestController
@RequestMapping("/api/camion")
public class CamionController {

    private final ICamionService camionService;
    private final IDocumentoService documentoService;

    public CamionController(
            ICamionService camionService,
            IDocumentoService documentoService
    ) {
        this.camionService = camionService;
        this.documentoService = documentoService;
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
    public ResponseEntity<List<DocumentoDTOResponse>> listarDocumentos(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.listarDocumentos(id, RolDocumento.CAMION));
    }

    @GetMapping("/tiposDocumento")
    public ResponseEntity<?> listarTiposDocumento(){
        return ResponseEntity.ok(camionService.listarTiposDocumento());
    }

    @PostMapping(
            value = "/{id}/documentos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentoDTOResponse> registrarDocumento(
            @PathVariable Long id,
            @Valid @RequestPart("datos") DocumentoDTORequest datos,
            @RequestPart("archivo") MultipartFile archivo) {
        System.out.println("Tipo documento: " + datos.idTipoDocumento());
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.registrarDocumento(id, datos, archivo, RolDocumento.CAMION));
    }

    @GetMapping("/{id}/documento/{idDocumento}")
    public ResponseEntity<String> obtenerFoto(@PathVariable Long id, @PathVariable Long idDocumento) {
        String url = documentoService.obtenerArchivoURL(id, idDocumento, RolDocumento.CAMION);
        return ResponseEntity.ok(url);
    }

    @PutMapping(
            value = "/{id}/documentos/{idDocumento}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentoDTOResponse> actualizarDocumento(
            @PathVariable Long id,
            @PathVariable Long idDocumento,
            @Valid @RequestPart("datos") DocumentoDTORequest datos,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo
    ) {
        System.out.println(datos);
        return ResponseEntity.ok(documentoService.actualizarDocumento(id, idDocumento, datos, archivo, RolDocumento.CAMION));
    }

    @DeleteMapping("/{id}/documentos/{idDocumento}")
    public ResponseEntity<Void> eliminarDocumento(
            @PathVariable Long id,
            @PathVariable Long idDocumento
    ) {
        documentoService.eliminar(id, idDocumento, RolDocumento.CAMION);
        return ResponseEntity.noContent().build();
    }
}
