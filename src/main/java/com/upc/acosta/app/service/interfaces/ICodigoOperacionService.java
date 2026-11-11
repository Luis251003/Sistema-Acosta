package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.CodigoDTOResponse;

public interface ICodigoOperacionService {
    CodigoDTOResponse obtenerCodigo(Long usuarioId);
    void confirmarUso(Long usuarioId, Long id);
}
