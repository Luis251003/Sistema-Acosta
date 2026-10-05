package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.Rol;

import java.util.Set;

public record UsuarioDTOResponse (
        Long id,
        String email,
        Set<Rol> roles
){
}
