package com.upc.acosta.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record OperacionDTORequest(
        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,
        @NotNull(message = "La ubicación de partida es obligatoria")
        @Valid
        UbicacionDTORequest ubicacionPartida,
        @NotNull(message = "La ubicación de destino es obligatoria")
        @Valid
        UbicacionDTORequest ubicacionDestino,
        @Size(max = 300, message = "La observación no puede superar los 300 caracteres")
        String observacion,
        @NotNull(message = "El codigo es obligatorio")
        Long idCodigo,
        @NotNull(message = "La fecha de recojo es obligatoria")
        LocalDate fechaRecojo,
        @NotNull(message = "El resultado de origen es obligatorio")
        @Valid
        ResultadoOrigen resultadoOrigen
) {
}
