package com.upc.acosta.app.dto;

public record ClienteDTOResponse (
        long id,
        String razonSocial,
        String ruc,
        String estado,
        String condicion,
        String direccion
){
}
