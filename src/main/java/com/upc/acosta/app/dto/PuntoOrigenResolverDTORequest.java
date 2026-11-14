package com.upc.acosta.app.dto;

import jakarta.validation.constraints.NotBlank;

public record PuntoOrigenResolverDTORequest(
        @NotBlank(message = "La dirección es obligatoria")
        String direccion
) {
}
