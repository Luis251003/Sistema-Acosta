package com.upc.acosta.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DocumentoDTORequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,
        @NotNull(message = "El tipo de documento es obligatorio")
        Long idTipoDocumento,
        @NotNull(message = "La fecha de emisión es obligatoria")
        LocalDate fechaEmision,
        @NotNull(message = "La fecha de vencimiento es obligatoria")
        LocalDate fechaVencimiento
) {
}
