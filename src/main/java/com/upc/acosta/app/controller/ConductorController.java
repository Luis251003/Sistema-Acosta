package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.*;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.entity.tipos.TipoConductor;
import com.upc.acosta.app.service.interfaces.IConductorService;
import com.upc.acosta.app.service.interfaces.IDocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/conductor")
@RequiredArgsConstructor
public class ConductorController {

    // DECLARAMOS LOS SERVICIOS
    private final IConductorService conductorService;
    private final IDocumentoService documentoService;

    // ::::::::::::::::::::::::::: CRUD CONDUCTOR :::::::::::::::::::::::::::::::

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) TipoConductor tipoConductor
            ){
        List<ConductorDTOResponse> conductores = conductorService.listar(busqueda, tipoConductor);
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

    // ::::::::::::::::::::::::::: BUSQUEDAS :::::::::::::::::::::::::::::::

    // BUSCAR CONDUCTOR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerId(@PathVariable Long id){
        ConductorDTOResponse bean = conductorService.buscarXId(id);
        return ResponseEntity.ok(bean);
    }

    @GetMapping(value = "/seleccionar", params = {"idConductor","idOperacion"})
    public ResponseEntity<?> buscarXIdAndDisponibilidad(
            @RequestParam Long idConductor,
            @RequestParam Long idOperacion){
        ConductorSeleccionadoDTOResponse conductor = conductorService.buscarXIdAndDisponibilidad(idConductor,idOperacion);
        return ResponseEntity.ok(conductor);
    }

    // BUSCAR CONDUCTOR POR BREVETE
    @GetMapping(value = "/seleccionar", params = {"brevete","idOperacion"})
    public ResponseEntity<?> buscarXBreveteAndDisponibilidad(
            @RequestParam String brevete,
            @RequestParam Long idOperacion){
        ConductorSeleccionadoDTOResponse conductor = conductorService.buscarXBreveteAndDisponibilidad(brevete,idOperacion);
        return ResponseEntity.ok(conductor);
    }

    // ::::::::::::::::::::::::::: METODOS PARA LA FOTO :::::::::::::::::::::::::::::::

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

    // ::::::::::::::::::::::::::: CRUD PARA DOCUMENTOS :::::::::::::::::::::::::::::::

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

    // ::::::::::::::::::::::::::: METODOS DE AYUDA :::::::::::::::::::::::::::::::

    @GetMapping("/{id}/documento/{idDocumento}")
    public ResponseEntity<String> obtenerArchivo(@PathVariable Long id, @PathVariable Long idDocumento) {
        String url = documentoService.obtenerArchivoURL(id, idDocumento, RolDocumento.CONDUCTOR);
        return ResponseEntity.ok(url);
    }

    @PutMapping("/{id}/documentos/actualizar-estados")
    public ResponseEntity<?> actualizarEstadosDocumentos(@PathVariable Long idEntidad){
        documentoService.actualizarEstadosDocumentos(idEntidad, RolDocumento.CONDUCTOR);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tiposDocumento")
    public ResponseEntity<?> listarTiposDocumento(){
        return ResponseEntity.ok(conductorService.listarTiposDocumento());
    }
}
