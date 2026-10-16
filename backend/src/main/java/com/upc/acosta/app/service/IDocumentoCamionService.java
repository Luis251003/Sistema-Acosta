package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoCamionDTORequest;
import com.upc.acosta.app.dto.DocumentoCamionDTOResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentoCamionService {
    List<DocumentoCamionDTOResponse> listar(Long idCamion);
    byte[] obtenerArchivo(Long idDocumento);
    DocumentoCamionDTOResponse registrar(
            Long idCamion,
            DocumentoCamionDTORequest request,
            MultipartFile archivo
    );
    DocumentoCamionDTOResponse actualizar(
            Long idCamion,
            Long idDocumento,
            DocumentoCamionDTORequest request,
            MultipartFile archivo
    );
    void eliminar(Long idCamion, Long idDocumento);
}
