package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.FlotaDTORequest;
import com.upc.acosta.app.dto.FlotaDTOResponse;

public interface IFlotaService {
    FlotaDTOResponse registrar(FlotaDTORequest flota);
    FlotaDTOResponse actualizar(FlotaDTORequest flota);
}
