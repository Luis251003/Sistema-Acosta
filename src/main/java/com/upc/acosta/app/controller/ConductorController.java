package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.*;
import com.upc.acosta.app.entity.RolDocumento;
import com.upc.acosta.app.entity.TipoConductor;
import com.upc.acosta.app.service.IConductorService;
import com.upc.acosta.app.service.IDocumentoService;
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
    private final IDocumentoService documentoService;

    public ConductorController(
            IConductorService conductorService,
            IDocumentoService documentoService
    ) {
        this.conductorService = conductorService;
        this.documentoService = documentoService;
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) TipoConductor tipoConductor
            ){
        List<ConductorDTOResponse> conductores = conductorService.listar(busqueda, tipoConductor);
        return ResponseEntity.ok(conductores);
    }

    @GetMapping("/tiposDocumento")
    public ResponseEntity<?> listarTiposDocumento(){
        return ResponseEntity.ok(conductorService.listarTiposDocumento());
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
        conductorService.actualizarFoto(id, foto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<String> obtenerFoto(@PathVariable Long id) {
        String url = conductorService.obtenerURLFoto(id);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{id}/documentos")
    public ResponseEntity<List<DocumentoDTOResponse>> listarDocumentos(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.listarDocumentos(id, RolDocumento.CONDUCTOR));
    }

    @PostMapping(
            value = "/{id}/documentos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentoDTOResponse> registrarDocumento(
            @PathVariable Long id,
            @Valid @RequestPart("datos") DocumentoDTORequest datos,
            @RequestPart("archivo") MultipartFile archivo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.registrarDocumento(id, datos, archivo, RolDocumento.CONDUCTOR));
    }

    @GetMapping("/{id}/documento/{idDocumento}")
    public ResponseEntity<String> obtenerFoto(@PathVariable Long id, @PathVariable Long idDocumento) {
        String url = documentoService.obtenerArchivoURL(id, idDocumento, RolDocumento.CONDUCTOR);
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
        return ResponseEntity.ok(documentoService.actualizarDocumento(id, idDocumento, datos, archivo, RolDocumento.CONDUCTOR));
    }

    @DeleteMapping("/{id}/documentos/{idDocumento}")
    public ResponseEntity<Void> eliminarDocumento(
            @PathVariable Long id,
            @PathVariable Long idDocumento
    ) {
        documentoService.eliminar(id, idDocumento, RolDocumento.CONDUCTOR);
        return ResponseEntity.noContent().build();
    }
}
