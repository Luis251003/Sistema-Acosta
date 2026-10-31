package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.ConductorDTORequest;
import com.upc.acosta.app.dto.ConductorDTOResponse;
import com.upc.acosta.app.dto.TipoDocumentoDTOResponse;
import com.upc.acosta.app.entity.TipoConductor;
import com.upc.acosta.app.entity.TipoDocumento;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IConductorService {
    List<ConductorDTOResponse> listar(String busqueda, TipoConductor tipoConductor);
    List<TipoConductor> listarTipos();
    Set<TipoDocumentoDTOResponse> listarTiposDocumento();
    ConductorDTOResponse buscarXId(Long id);
    ConductorDTOResponse registrar(ConductorDTORequest bean);
    ConductorDTOResponse actualizar(ConductorDTORequest bean,Long id);
    List<ConductorDTOResponse> buscarXNombreOBrevete(String texto);
    void eliminar(Long id);
    void actualizarFoto(Long id, MultipartFile foto);
    String obtenerURLFoto(Long id);
}
