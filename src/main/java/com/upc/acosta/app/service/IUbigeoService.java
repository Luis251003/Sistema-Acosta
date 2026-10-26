package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.UbigeoDTOResponse;

import java.util.List;

public interface IUbigeoService {
    List<UbigeoDTOResponse> listarDepartamentos();

    List<UbigeoDTOResponse> listarProvincias(String codigoDepartamento);

    List<UbigeoDTOResponse> listarDistritos(String codigoProvincia);
}
