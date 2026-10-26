package com.upc.acosta.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDTORequest(
        @Email(message = "Ingresar un correo electrónico válido")
        @NotBlank(message = "El correo electrónico es obligatorio")
        String email,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8,max = 32, message = "La contraseña debe tener entre 8 y 32 caracteres")
        String password
) {
}
