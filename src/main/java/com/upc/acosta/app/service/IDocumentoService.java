package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.dto.DocumentoDTOResponse;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentoService {
    List<DocumentoDTOResponse> listarDocumentos(Long id, RolDocumento rol);
    DocumentoDTOResponse registrarDocumento(
            Long id,
            DocumentoDTORequest request,
            MultipartFile archivo,
            RolDocumento rol
    );
    String obtenerArchivoURL(Long idEntidad, Long idDocumento, RolDocumento rol);
    DocumentoDTOResponse actualizarDocumento(
            Long idEntidad,
            Long idDocumento,
            DocumentoDTORequest request,
            MultipartFile archivo,
            RolDocumento rol
    );
    void eliminar(Long idEntidad, Long idDocumento, RolDocumento rolDocumento);
}
