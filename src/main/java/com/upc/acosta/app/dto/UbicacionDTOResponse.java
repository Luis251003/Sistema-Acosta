package com.upc.acosta.app.dto;

import java.math.BigDecimal;

public record UbicacionDTOResponse(
        Long id,
        UbigeoCompletoDTOResponse ubigeo,
        BigDecimal latitud,
        BigDecimal longitud,
        String direccion
) {
}
