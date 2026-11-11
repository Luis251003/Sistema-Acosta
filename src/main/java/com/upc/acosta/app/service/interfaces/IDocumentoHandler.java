package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.entity.Documento;
import com.upc.acosta.app.entity.TipoDocumento;
import com.upc.acosta.app.entity.tipos.RolDocumento;

import java.util.List;

public interface IDocumentoHandler {
    RolDocumento rol();
    String carpeta();                                    // "camion" / "conductor"
    List<? extends Documento> listar(Long idEntidad);
    Documento obtener(Long idEntidad, Long idDocumento);
    void validarTipoUnico(Long idEntidad, Long idTipoDocumento);
    Documento crear(Long idEntidad, DocumentoDTORequest request, TipoDocumento tipo, String ruta);
    Documento guardar(Documento documento);
    void eliminar(Documento documento);
    void actualizarHabilitacion(Long idEntidad);
    List<Long> listarIdsEntidades();
}
