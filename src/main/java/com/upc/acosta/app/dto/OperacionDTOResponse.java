package com.upc.acosta.app.dto;

import java.time.LocalDate;

public record OperacionDTOResponse (
        Long id,
        ClienteDTOResponse cliente,
        UbicacionDTOResponse partida,
        UbicacionDTOResponse destino,
        String puntoOrigen,
        String codigoOperacion,
        String estado,
        String observacion,
        String tokenSeguimiento,
        LocalDate recojoProgramado
){
}
