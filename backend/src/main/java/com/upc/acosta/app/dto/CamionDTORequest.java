package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.TipoVehiculo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CamionDTORequest(
        @NotBlank(message = "La placa es obligatoria")
        @Size(max = 10, message = "La placa no puede superar los 10 caracteres")
        String placa,
        @NotBlank(message = "La marca es obligatoria")
        @Size(max = 50, message = "La marca no puede superar los 50 caracteres")
        String marca,
        @NotBlank(message = "El modelo es obligatorio")
        @Size(max = 50, message = "El modelo no puede superar los 50 caracteres")
        String modelo,
        @NotBlank(message = "La capacidad es obligatoria")
        @DecimalMin(value = "0.01", message = "La capacidad debe ser mayor que cero")
        @Digits(integer = 8, fraction = 2, message = "La capacidad admite hasta 8 dígitos enteros y 2 decimales")
        String capacidad,
        @NotBlank(message = "El número TUCE es obligatorio")
        @Size(max = 30, message = "El número TUCE no puede superar los 30 caracteres")
        String numeroTuce,
        @NotNull(message = "El tipo de vehículo es obligatorio")
        TipoVehiculo tipoVehiculo
){
}
