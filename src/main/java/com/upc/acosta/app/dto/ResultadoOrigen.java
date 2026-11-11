package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.PuntoOrigen;
import com.upc.acosta.app.entity.tipos.MetodoResolucion;
import jakarta.validation.constraints.NotNull;

public record ResultadoOrigen(PuntoOrigen punto, @NotNull(message = "El método de resolución es obligatorio") MetodoResolucion metodo) {
    public static ResultadoOrigen alias (PuntoOrigen p){
        return new ResultadoOrigen(p, MetodoResolucion.ALIAS);
    }
    public static ResultadoOrigen regla(PuntoOrigen p){
        return new ResultadoOrigen(p, MetodoResolucion.REGLA);
    }
    public static ResultadoOrigen noEncontrado(){
        return new ResultadoOrigen(null, MetodoResolucion.NO_ENCONTRADO);
    }
}
