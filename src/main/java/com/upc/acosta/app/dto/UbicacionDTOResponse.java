package com.upc.acosta.app.dto;

import java.math.BigDecimal;

public record UbicacionDTOResponse(
        Integer id,
        UbigeoDTOResponse ubigeo,
        String direccionDetallada,
        BigDecimal latitud,
        BigDecimal longitud,
        String direccion
) {
}
