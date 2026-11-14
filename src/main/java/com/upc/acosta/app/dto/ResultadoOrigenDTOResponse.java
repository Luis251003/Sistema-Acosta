package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.tipos.MetodoResolucion;

public record ResultadoOrigenDTOResponse(
        PuntoOrigenDTOResponse punto,
        MetodoResolucion metodo
) {
}
