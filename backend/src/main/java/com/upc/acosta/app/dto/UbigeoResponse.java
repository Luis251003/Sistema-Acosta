package com.upc.acosta.app.dto;

public record UbigeoResponse(
        String id,
        String departamentoCodigo,
        String departamento,
        String provinciaCodigo,
        String provincia,
        String distrito
) {
}
