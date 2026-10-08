package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.ConductorDTORequest;
import com.upc.acosta.app.dto.ConductorDTOResponse;
import com.upc.acosta.app.dto.DocumentoConductorDTORequest;
import com.upc.acosta.app.dto.DocumentoConductorDTOResponse;
import com.upc.acosta.app.service.IDocumentoConductorService;
import com.upc.acosta.app.service.IConductorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/conductor")
public class ConductorController {

    private final IConductorService conductorService;
    private final IDocumentoConductorService documentoConductorService;

    public ConductorController(
            IConductorService conductorService,
            IDocumentoConductorService documentoConductorService
    ) {
        this.conductorService = conductorService;
        this.documentoConductorService = documentoConductorService;
    }

    @GetMapping
    public ResponseEntity<?> listar(){
        List<ConductorDTOResponse> conductores = conductorService.listar();
        return ResponseEntity.ok(conductores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerId(@PathVariable Long id){
        ConductorDTOResponse bean = conductorService.buscarXId(id);
        return ResponseEntity.ok(bean);
    }

    @GetMapping("/buscar/{texto}")
    public ResponseEntity<?> buscarXNombreOBrevete(@PathVariable String texto){
        List<ConductorDTOResponse> conductores = conductorService.buscarXNombreOBrevete(texto);
        return ResponseEntity.ok(conductores);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody ConductorDTORequest bean){
        ConductorDTOResponse conductor = conductorService.registrar(bean);
        return ResponseEntity.status(HttpStatus.CREATED).body(conductor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,@Valid @RequestBody ConductorDTORequest bean){
        ConductorDTOResponse conductor = conductorService.actualizar(bean,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(conductor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        conductorService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(
            value = "/{id}/foto",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> actualizarFoto(
            @PathVariable Long id,
            @RequestParam("foto") MultipartFile foto) {

        return ResponseEntity.ok(
                conductorService.actualizarFoto(id, foto)
        );
    }

    @GetMapping("/{id}/documentos")
    public ResponseEntity<List<DocumentoConductorDTOResponse>> listarDocumentos(@PathVariable Long id) {
        return ResponseEntity.ok(documentoConductorService.listar(id));
    }

    @GetMapping("/{id}/documentos/{idDocumento}")
    public ResponseEntity<DocumentoConductorDTOResponse> obtenerDocumento(
            @PathVariable Long id,
            @PathVariable Long idDocumento
    ) {
        return ResponseEntity.ok(documentoConductorService.buscar(id, idDocumento));
    }

    @PostMapping(value = "/{id}/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoConductorDTOResponse> registrarDocumento(
            @PathVariable Long id,
            @Valid @RequestPart("datos") DocumentoConductorDTORequest datos,
            @RequestPart("archivo") MultipartFile archivo
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentoConductorService.registrar(id, datos, archivo));
    }

    @PutMapping(value = "/{id}/documentos/{idDocumento}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoConductorDTOResponse> actualizarDocumento(
            @PathVariable Long id,
            @PathVariable Long idDocumento,
            @Valid @RequestPart("datos") DocumentoConductorDTORequest datos,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo
    ) {
        return ResponseEntity.ok(documentoConductorService.actualizar(id, idDocumento, datos, archivo));
    }

    @DeleteMapping("/{id}/documentos/{idDocumento}")
    public ResponseEntity<Void> eliminarDocumento(
            @PathVariable Long id,
            @PathVariable Long idDocumento
    ) {
        documentoConductorService.eliminar(id, idDocumento);
        return ResponseEntity.noContent().build();
    }
}
