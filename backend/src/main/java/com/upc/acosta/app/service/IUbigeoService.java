package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.UbigeoDTOOpcion;

import java.util.List;

public interface IUbigeoService {
    List<UbigeoDTOOpcion> listarDepartamentos();

    List<UbigeoDTOOpcion> listarProvincias(String codigoDepartamento);

    List<UbigeoDTOOpcion> listarDistritos(String codigoProvincia);
}
