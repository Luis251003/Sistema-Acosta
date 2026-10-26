package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.dto.DocumentoDTOResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentoService {
    List<DocumentoDTOResponse> listar(Long idCamion);
    byte[] obtenerArchivo(Long idDocumento);
    DocumentoDTOResponse registrar(
            Long idCamion,
            DocumentoDTORequest request,
            MultipartFile archivo
    );
    DocumentoDTOResponse actualizar(
            Long idCamion,
            Long idDocumento,
            DocumentoDTORequest request,
            MultipartFile archivo
    );
    void eliminar(Long idCamion, Long idDocumento);
}
