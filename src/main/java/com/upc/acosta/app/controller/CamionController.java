package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.*;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.entity.tipos.TipoVehiculo;
import com.upc.acosta.app.service.interfaces.IDocumentoService;
import jakarta.validation.Valid;
import com.upc.acosta.app.service.interfaces.ICamionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/camion")
@RequiredArgsConstructor
public class CamionController {

    // DECLARAMOS LOS SERVICIOS
    private final ICamionService camionService;
    private final IDocumentoService documentoService;

    // ::::::::::::::::::::::::::: CRUD CAMION :::::::::::::::::::::::::::::::

    @GetMapping("/{id}")
    public ResponseEntity<CamionDTOResponse> buscarXId(@PathVariable Long id) {
        return ResponseEntity.ok(camionService.buscarXId(id));
    }

    @GetMapping("/buscar/{texto}")
    public ResponseEntity<List<CamionDTOResponse>> buscarPorPlaca(@PathVariable String texto) {
        return ResponseEntity.ok(camionService.buscarPorPlaca(texto));
    }

    @GetMapping
    public ResponseEntity<List<CamionDTOResponse>> listar(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) TipoVehiculo tipoVehiculo
            ) {
        List<CamionDTOResponse> camiones = camionService.listar(busqueda,tipoVehiculo);
        return ResponseEntity.ok(camiones);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        camionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ::::::::::::::::::::::::::: SELECCIONAR CAMION :::::::::::::::::::::::::::::::

    // BUSCAR POR ID
    @GetMapping(value = "/seleccionar", params = {"idCamion","idOperacion"})
    public ResponseEntity<?> buscarXIdAndDisponibilidad(
        @RequestParam Long idCamion,
        @RequestParam Long idOperacion
    ){
        CamionSeleccionadoDTOResponse camion = camionService.buscarXIdAndDisponibilidad(idCamion,idOperacion);
        return ResponseEntity.ok(camion);
    }

    // BUSCAR POR PLACA
    @GetMapping(value = "/seleccionar", params = {"placa","idOperacion"})
    public ResponseEntity<?> buscarXPlacaAndDisponibilidad(
            @RequestParam String placa,
            @RequestParam Long idOperacion){
        CamionSeleccionadoDTOResponse camion = camionService.buscarXPlacaAndDisponibilidad(placa,idOperacion);
        return ResponseEntity.ok(camion);
    }

    // ::::::::::::::::::::::::::: CRUD DOCUMENTOS :::::::::::::::::::::::::::::::

    @GetMapping("/{id}/documentos")
    public ResponseEntity<List<DocumentoDTOResponse>> listarDocumentos(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.listarDocumentos(id, RolDocumento.CAMION));
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

    // ::::::::::::::::::::::::::: METODOS DE AYUDA :::::::::::::::::::::::::::::::

    @GetMapping("/{id}/documento/{idDocumento}")
    public ResponseEntity<String> obtenerFoto(@PathVariable Long id, @PathVariable Long idDocumento) {
        String url = documentoService.obtenerArchivoURL(id, idDocumento, RolDocumento.CAMION);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/tiposDocumento")
    public ResponseEntity<?> listarTiposDocumento(){
        return ResponseEntity.ok(camionService.listarTiposDocumento());
    }

    @PutMapping("/{id}/documentos/actualizar-estados")
    public ResponseEntity<?> actualizarEstadosDocumentos(@PathVariable Long idEntidad){
        documentoService.actualizarEstadosDocumentos(idEntidad, RolDocumento.CAMION);
        return ResponseEntity.ok().build();
    }

}
