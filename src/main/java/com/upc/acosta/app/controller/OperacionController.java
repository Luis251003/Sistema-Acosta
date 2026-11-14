package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.*;
import com.upc.acosta.app.entity.tipos.EstadoOperacion;
import com.upc.acosta.app.entity.tipos.TipoVehiculo;
import com.upc.acosta.app.security.UsuarioPrincipal;
import com.upc.acosta.app.service.interfaces.ICodigoOperacionService;
import com.upc.acosta.app.service.interfaces.IOperacionService;
import com.upc.acosta.app.service.interfaces.IPuntoOrigenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operacion")
@RequiredArgsConstructor
public class OperacionController {

    private final ICodigoOperacionService codigoService;
    private final IOperacionService operacionService;
    private final IPuntoOrigenService puntoOrigenService;

    @GetMapping("/codigo-nuevo")
    public ResponseEntity<?> codigoNuevo(@AuthenticationPrincipal UsuarioPrincipal user) {
        return ResponseEntity.ok(Map.of("codigo", codigoService.obtenerCodigo(user.getId())));
    }

    @GetMapping
    public ResponseEntity<?> listar(
        @RequestParam(required = false) String busqueda,
        @RequestParam(required = false) LocalDate fechaInicio,
        @RequestParam(required = false) LocalDate fechaFin,
        @RequestParam(required = false) List<String> placas,
        @RequestParam(required = false) List<EstadoOperacion> estados
    ){
        List<OperacionListarDTOResponse> operaciones = operacionService.listar(busqueda,fechaInicio,fechaFin,placas,estados);
        return ResponseEntity.ok(operaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarXId(@PathVariable Long id){
        OperacionCompletaDTOResponse operacion = operacionService.buscarXId(id);
        return ResponseEntity.ok(operacion);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody @Valid OperacionDTORequest request,
                                       @AuthenticationPrincipal UsuarioPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operacionService.registrar(request, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody @Valid OperacionDTORequest request,
                                        @AuthenticationPrincipal UsuarioPrincipal user) {
        return ResponseEntity.ok(operacionService.actualizar(request, id, user.getId()));
    }

}
