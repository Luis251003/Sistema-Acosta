package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.PuntoOrigenDTOResponse;
import com.upc.acosta.app.dto.ResultadoOrigen;
import com.upc.acosta.app.entity.PuntoOrigen;

import java.util.Optional;

public interface IPuntoOrigenService {
    ResultadoOrigen resolver(String direccionCruda);
    Optional<PuntoOrigen> buscarPorAlias(String direccionCruda);
    Optional<PuntoOrigen> buscarPorRegla(String direccionCruda);

    PuntoOrigenDTOResponse entityToResponse(PuntoOrigen puntoOrigen);
}
