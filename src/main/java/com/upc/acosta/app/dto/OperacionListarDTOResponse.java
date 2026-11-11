package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.tipos.EstadoOperacion;

import java.time.LocalDate;

public record OperacionListarDTOResponse(
        Long id,
        String codigoOperacion,
        String cliente,
        LocalDate fechaViaje,
        String conductor,
        String placa,
        EstadoOperacion estado
) {
}
