package com.upc.acosta.app.dto;

public record ConductorDTOResponse(
        Long id,
        String nombre,
        String apellido,
        String telefono,
        String dni,
        String brevete,
        String fotoUrl
) {
}
