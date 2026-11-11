package com.upc.acosta.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UbicacionDTORequest (
        @NotNull(message = "El código de ubigeo es obligatorio")
        String ubigeo,
        @NotEmpty(message = "La dirección es obligatoria")
        String direccion,
        @NotNull(message = "La latitud es obligatoria")
        BigDecimal latitud,
        @NotNull(message = "La longitud es obligatoria")
        BigDecimal longitud
){
}
