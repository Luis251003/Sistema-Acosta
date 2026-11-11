package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.ConductorDTORequest;
import com.upc.acosta.app.dto.ConductorDTOResponse;
import com.upc.acosta.app.dto.ConductorSeleccionadoDTOResponse;
import com.upc.acosta.app.dto.TipoDocumentoDTOResponse;
import com.upc.acosta.app.entity.tipos.TipoConductor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IConductorService {

    // SERVICIOS CRUD
    List<ConductorDTOResponse> listar(String busqueda, TipoConductor tipoConductor);
    ConductorDTOResponse buscarXId(Long id);
    ConductorDTOResponse registrar(ConductorDTORequest bean);
    ConductorDTOResponse actualizar(ConductorDTORequest bean,Long id);
    void eliminar(Long id);

    //SERVICIOS DE BUSQUEDA
    ConductorSeleccionadoDTOResponse buscarXIdAndDisponibilidad(Long idConductor, Long idOperacion);
    ConductorSeleccionadoDTOResponse buscarXBreveteAndDisponibilidad(String brevete,Long idOperacion);

    // SERVICIOS DE FOTO
    void actualizarFoto(Long id, MultipartFile foto);
    String obtenerURLFoto(Long id);

    Set<TipoDocumentoDTOResponse> listarTiposDocumento();
}
