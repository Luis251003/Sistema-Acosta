package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.CamionDTORequest;
import com.upc.acosta.app.dto.CamionDTOResponse;
import com.upc.acosta.app.dto.TipoDocumentoDTOResponse;
import com.upc.acosta.app.entity.tipos.TipoVehiculo;

import java.util.List;
import java.util.Set;

public interface ICamionService {
    CamionDTOResponse registrar(CamionDTORequest request);
    CamionDTOResponse actualizar(Long id, CamionDTORequest request);
    List<CamionDTOResponse> listar(String busqueda, TipoVehiculo tipoVehiculo);
    Set<TipoDocumentoDTOResponse> listarTiposDocumento();
    CamionDTOResponse buscarXId(Long id);
    List<CamionDTOResponse> buscarPorPlaca(String placa);
    void eliminar(Long id);
}
