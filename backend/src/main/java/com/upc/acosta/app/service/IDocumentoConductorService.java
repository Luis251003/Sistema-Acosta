package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoConductorDTORequest;
import com.upc.acosta.app.dto.DocumentoConductorDTOResponse;
import com.upc.acosta.app.entity.TipoDocumentoConductor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentoConductorService {
    List<TipoDocumentoConductor> listarTipos();
    List<DocumentoConductorDTOResponse> listar(Long idConductor);
    DocumentoConductorDTOResponse buscar(Long idConductor, Long idDocumento);
    DocumentoConductorDTOResponse registrar(Long idConductor, DocumentoConductorDTORequest request, MultipartFile archivo);
    DocumentoConductorDTOResponse actualizar(Long idConductor, Long idDocumento, DocumentoConductorDTORequest request, MultipartFile archivo);
    void eliminar(Long idConductor, Long idDocumento);
}
