package com.upc.acosta.app.dto;

import java.util.List;

public record FlotaDTOResponse(
        Long id,
        List<FlotaDetalleDTOResponse> detalles
) {
}
