package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.CamionDTORequest;
import com.upc.acosta.app.dto.CamionDTOResponse;

import java.util.List;

public interface ICamionService {
    CamionDTOResponse registrar(CamionDTORequest request);
    CamionDTOResponse actualizar(Long id, CamionDTORequest request);
    List<CamionDTOResponse> listar();
    CamionDTOResponse buscarXId(Long id);
    List<CamionDTOResponse> buscarXNombreOPlaca(String texto);
}
