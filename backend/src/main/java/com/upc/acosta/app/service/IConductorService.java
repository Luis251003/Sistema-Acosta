package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.ConductorDTORequest;
import com.upc.acosta.app.dto.ConductorDTOResponse;
import com.upc.acosta.app.entity.TipoConductor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IConductorService {
    List<ConductorDTOResponse> listar();
    List<TipoConductor> listarTipos();
    ConductorDTOResponse buscarXId(Long id);
    ConductorDTOResponse registrar(ConductorDTORequest bean);
    ConductorDTOResponse actualizar(ConductorDTORequest bean,Long id);
    List<ConductorDTOResponse> buscarXNombreOBrevete(String texto);
    void eliminar(Long id);
    String actualizarFoto(Long id, MultipartFile foto);
}
