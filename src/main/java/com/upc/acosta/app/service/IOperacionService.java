package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.RucDTOResponse;
import com.upc.acosta.app.entity.Operacion;

public interface IOperacionService {
    RucDTOResponse buscarClientePorRuc(String ruc);

    String generarCodigoServicio(Long numeroOperacion);

    Operacion registrar(Operacion operacion);
}
