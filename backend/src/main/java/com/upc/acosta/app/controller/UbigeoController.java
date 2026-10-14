package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.UbigeoDTOOpcion;
import com.upc.acosta.app.service.IUbigeoService;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ubigeo")
@Validated
public class UbigeoController {
    private final IUbigeoService ubigeoService;

    public UbigeoController(IUbigeoService ubigeoService) {
        this.ubigeoService = ubigeoService;
    }

    @GetMapping("/departamentos")
    public List<UbigeoDTOOpcion> listarDepartamentos() {
        return ubigeoService.listarDepartamentos();
    }

    @GetMapping("/provincias/{codigoDepartamento}")
    public List<UbigeoDTOOpcion> listarProvincias(
            @PathVariable @Pattern(regexp = "\\d{2}") String codigoDepartamento
    ) {
        return ubigeoService.listarProvincias(codigoDepartamento);
    }

    @GetMapping("/distritos/{codigoProvincia}")
    public List<UbigeoDTOOpcion> listarDistritos(
            @PathVariable @Pattern(regexp = "\\d{4}") String codigoProvincia
    ) {
        return ubigeoService.listarDistritos(codigoProvincia);
    }
}
