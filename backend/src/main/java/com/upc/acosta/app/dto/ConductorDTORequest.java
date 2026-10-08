package com.upc.acosta.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ConductorDTORequest (
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
        String apellido,
        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(
                regexp = "^9\\d{8}$",
                message = "El teléfono debe tener 9 dígitos y comenzar con 9"
        )
        String telefono,
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(
                regexp = "^[0-9]{8}$",
                message = "El DNI debe contener 8 caracteres numéricos"
        )
        String dni,
        @NotBlank(message = "El brevete es obligatorio")
        @Pattern(
                regexp = "^[A-Z0-9]{8,10}$",
                message = "El brevete debe contener entre 8 y 10 caracteres alfanuméricos"
        )
        String brevete
){
}
