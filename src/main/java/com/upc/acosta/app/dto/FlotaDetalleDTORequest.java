package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.tipos.RolFlota;
import jakarta.validation.constraints.NotNull;

public record FlotaDetalleDTORequest(
        @NotNull(message = "El id del conductor es obligatorio")
        Long idConductor,
        @NotNull(message = "El id del camion es obligatorio")
        Long idCamion,
        @NotNull(message = "El rol de la flota es obligatorio")
        RolFlota rol
) {
}
