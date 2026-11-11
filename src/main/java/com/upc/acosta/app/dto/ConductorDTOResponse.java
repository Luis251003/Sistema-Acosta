package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.tipos.TipoConductor;

public record ConductorDTOResponse(
        Long id,
        String nombre,
        String apellido,
        String telefono,
        String dni,
        String brevete,
        String fotoUrl,
        TipoConductor tipoConductor,
        Boolean habilitado
) {
}
