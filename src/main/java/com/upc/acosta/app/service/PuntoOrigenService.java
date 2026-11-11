package com.upc.acosta.app.service;

import com.upc.acosta.app.config.ReglasPuntoOrigen;
import com.upc.acosta.app.dto.PuntoOrigenDTOResponse;
import com.upc.acosta.app.dto.ResultadoOrigen;
import com.upc.acosta.app.entity.DireccionNormalizar;
import com.upc.acosta.app.entity.PuntoOrigen;
import com.upc.acosta.app.entity.PuntoOrigenAlias;
import com.upc.acosta.app.repository.IPuntoOrigenAliasRepository;
import com.upc.acosta.app.repository.IPuntoOrigenRepository;
import com.upc.acosta.app.service.interfaces.IPuntoOrigenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PuntoOrigenService implements IPuntoOrigenService {

    //Repositorios
    private final IPuntoOrigenAliasRepository puntoOrigenAliasRepository;
    private final IPuntoOrigenRepository puntoRepo;
    //Reglas
    private final ReglasPuntoOrigen reglas;

    @Override
    public ResultadoOrigen resolver(String direccionCruda) {
        // Paso 1: formatear
        String direccion = DireccionNormalizar.normalizar(direccionCruda);

        // Paso 2: alias
        Optional<PuntoOrigen> porAlias = buscarPorAlias(direccion);
        if (porAlias.isPresent()) {
            return ResultadoOrigen.alias(porAlias.get());
        }

        // Paso 3: regla
        Optional<PuntoOrigen> porRegla = buscarPorRegla(direccion);
        return porRegla.map(ResultadoOrigen::regla).orElseGet(ResultadoOrigen::noEncontrado);

        // Paso 4: no encontrado
    }

    @Override
    public Optional<PuntoOrigen> buscarPorAlias(String direccion) {
        return puntoOrigenAliasRepository.findByDireccionNormalizada(direccion).map(PuntoOrigenAlias::getPuntoOrigen);
    }

    @Override
    public Optional<PuntoOrigen> buscarPorRegla(String direccion) {
        return reglas.buscarPuntoOrigen(direccion).flatMap(puntoRepo::findByNombre);
    }

    @Override
    public PuntoOrigenDTOResponse entityToResponse(PuntoOrigen puntoOrigen) {
        return new PuntoOrigenDTOResponse(puntoOrigen.getNombre());
    }

}
