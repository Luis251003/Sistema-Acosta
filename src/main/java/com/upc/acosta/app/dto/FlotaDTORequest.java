package com.upc.acosta.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record FlotaDTORequest (
        @NotNull(message = "El id de la operación es obligatorio")
        Long idOperacion,
        @NotNull(message = "El detalle principal es obligatorio")
        @Valid
        FlotaDetalleDTORequest principal,
        @Valid
        FlotaDetalleDTORequest secundario
){ }
