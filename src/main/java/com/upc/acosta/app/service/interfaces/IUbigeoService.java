package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.UbigeoDTOResponse;

import java.util.List;

public interface IUbigeoService {
    List<UbigeoDTOResponse> listarDepartamentos();

    List<UbigeoDTOResponse> listarProvincias(Long idDepartamento);

    List<UbigeoDTOResponse> listarDistritos(Long idProvincia);
}
