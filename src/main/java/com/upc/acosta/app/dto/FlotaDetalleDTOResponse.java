package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.tipos.RolFlota;

public record FlotaDetalleDTOResponse(
        Long id,
        String conductor,
        String placaCamion,
        RolFlota rol
) {
}
