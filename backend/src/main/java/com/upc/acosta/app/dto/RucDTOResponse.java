package com.upc.acosta.app.dto;

public record RucDTOResponse(
        String ruc,
        String razonSocial,
        String estado,
        String condicion,
        String direccion
) {
}
