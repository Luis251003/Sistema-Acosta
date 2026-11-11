package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.RucDTOResponse;
import com.upc.acosta.app.service.interfaces.IRucService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ruc")
public class RucController {

    private final IRucService rucService;

    private RucController(IRucService rucService){
        this.rucService = rucService;
    }

    @GetMapping("/{ruc}")
    public ResponseEntity<?> buscarXRuc(@PathVariable String ruc){
        RucDTOResponse rucResponse = rucService.consultarRUC(ruc);
        return ResponseEntity.ok(rucResponse);
    }
    
}
