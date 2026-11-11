package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.*;
import com.upc.acosta.app.entity.tipos.TipoVehiculo;

import java.util.List;
import java.util.Set;

public interface ICamionService {
    // SERVICIOS CRUD
    CamionDTOResponse buscarXId(Long id);
    CamionDTOResponse registrar(CamionDTORequest request);
    CamionDTOResponse actualizar(Long id, CamionDTORequest request);
    List<CamionDTOResponse> listar(String busqueda, TipoVehiculo tipoVehiculo);

    // SERVICIOS DE BUSQUEDA
    CamionSeleccionadoDTOResponse buscarXIdAndDisponibilidad(Long idCamion, Long idOperacion);
    CamionSeleccionadoDTOResponse buscarXPlacaAndDisponibilidad(String placa,Long idOperacion);

    // OTROS SERVICIOS
    Set<TipoDocumentoDTOResponse> listarTiposDocumento();
    List<CamionDTOResponse> buscarPorPlaca(String placa);
    void eliminar(Long id);
}
