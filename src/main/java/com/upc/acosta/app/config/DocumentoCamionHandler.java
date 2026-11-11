package com.upc.acosta.app.config;

import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.entity.Camion;
import com.upc.acosta.app.entity.Documento;
import com.upc.acosta.app.entity.DocumentoCamion;
import com.upc.acosta.app.entity.TipoDocumento;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.ICamionRepository;
import com.upc.acosta.app.repository.IDocumentosCamionRepository;
import com.upc.acosta.app.repository.ITipoDocumentoRepository;
import com.upc.acosta.app.service.interfaces.IDocumentoHandler;
import com.upc.acosta.app.utils.DocumentoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DocumentoCamionHandler implements IDocumentoHandler {

    private final IDocumentosCamionRepository documentoRepo;
    private final ICamionRepository camionRepo;
    private final ITipoDocumentoRepository tipoRepo;

    @Override public RolDocumento rol() { return RolDocumento.CAMION; }
    @Override public String carpeta()   { return "camion"; }

    private Camion camion(Long id) {
        return camionRepo.findById(id)
                .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                .orElseThrow(() -> new NotFoundException("Camión con ID: " + id + " no encontrado"));
    }

    @Override
    public List<? extends Documento> listar(Long idEntidad) {
        camion(idEntidad);
        return documentoRepo.findByCamionId(idEntidad);
    }

    @Override
    public Documento obtener(Long idEntidad, Long idDocumento) {
        return documentoRepo.findByIdAndCamionId(idDocumento, idEntidad)
                .orElseThrow(() -> new NotFoundException(
                        "Documento con ID: " + idDocumento + " no encontrado para el camión " + idEntidad));
    }

    @Override
    public void validarTipoUnico(Long idEntidad, Long idTipoDocumento) {
        if (documentoRepo.existsByCamionIdAndTipoDocumentoIdTipoDocumento(idEntidad, idTipoDocumento)) {
            throw new BadRequestException("El tipo de documento ya está registrado para este camión");
        }
    }

    @Override
    public Documento crear(Long idEntidad, DocumentoDTORequest request, TipoDocumento tipo, String ruta) {
        return documentoRepo.saveAndFlush(new DocumentoCamion(
                request.nombre().trim(), tipo, ruta, request.fechaEmision(), request.fechaVencimiento(), camion(idEntidad)));
    }

    @Override
    public Documento guardar(Documento documento) {
        return documentoRepo.saveAndFlush((DocumentoCamion) documento);
    }

    @Override
    public void eliminar(Documento documento) {
        documentoRepo.delete((DocumentoCamion) documento);
        documentoRepo.flush();
    }

    @Override
    public void actualizarHabilitacion(Long idEntidad) {
        Camion camion = camion(idEntidad);
        Set<TipoDocumento> requeridos = tipoRepo.findByRol(RolDocumento.CAMION);
        camion.setHabilitado(DocumentoUtils.isHabilitado(requeridos, documentoRepo.findByCamionId(idEntidad)));
        camionRepo.save(camion);
    }

    @Override
    public List<Long> listarIdsEntidades() {
        return camionRepo.findAllIds();
    }

}
