package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.UbigeoDTOResponse;
import com.upc.acosta.app.service.interfaces.IUbigeoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ubigeo")
public class UbigeoController {

    private final IUbigeoService ubigeoService;

    public UbigeoController(IUbigeoService ubigeoService) {
        this.ubigeoService = ubigeoService;
    }

    @GetMapping("/departamentos")
    public ResponseEntity<List<UbigeoDTOResponse>> listarDepartamentos() {
        return ResponseEntity.ok(ubigeoService.listarDepartamentos());
    }

    @GetMapping("/departamentos/{idDepartamento}/provincias")
    public ResponseEntity<List<UbigeoDTOResponse>> listarProvincias(@PathVariable Long idDepartamento) {
        return ResponseEntity.ok(ubigeoService.listarProvincias(idDepartamento));
    }

    @GetMapping("/provincias/{idProvincia}/distritos")
    public ResponseEntity<List<UbigeoDTOResponse>> listarDistritos(@PathVariable Long idProvincia) {
        return ResponseEntity.ok(ubigeoService.listarDistritos(idProvincia));
    }

}
