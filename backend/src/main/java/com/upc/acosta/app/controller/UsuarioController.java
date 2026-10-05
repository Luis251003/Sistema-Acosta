package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.UsuarioDTOResponse;
import com.upc.acosta.app.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> buscarXId(@PathVariable String email){
        UsuarioDTOResponse bean = usuarioService.buscarXCorreo(email);
        return ResponseEntity.status(HttpStatus.OK).body(bean);
    }

}
