package com.upc.acosta.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AgenciaAduanasDTORequest (
        @NotBlank(message = "El RUC es obligatorio")
        @Pattern(
                regexp = "\\d{11}",
                message = "El RUC debe contener exactamente 11 dígitos"
        )
        String ruc,
        @NotBlank(message = "La razón social es obligatoria")
        @Size(
                max = 200,
                message = "La razón social no puede superar los 200 caracteres"
        )
        String razonSocial,
        @NotNull(message = "El estado es obligatorio")
        Long id_estado,
        @NotNull(message = "La condición es obligatoria")
        Long id_condicion
){
}
