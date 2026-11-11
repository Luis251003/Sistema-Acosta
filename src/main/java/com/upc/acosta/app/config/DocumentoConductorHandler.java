package com.upc.acosta.app.config;

import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.*;
import com.upc.acosta.app.service.interfaces.IDocumentoHandler;
import com.upc.acosta.app.utils.DocumentoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DocumentoConductorHandler implements IDocumentoHandler {

    //REPOSITORIOS
    private final IDocumentosConductorRepository documentoRepo;
    private final IConductorRepository conductorRepository;
    private final ITipoDocumentoRepository tipoRepo;

    @Override public RolDocumento rol() { return RolDocumento.CONDUCTOR; }
    @Override public String carpeta()   { return "conductor"; }

    private Conductor conductor(Long id) {
        return conductorRepository.findById(id)
                .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                .orElseThrow(() -> new NotFoundException("Conductor con ID: " + id + " no encontrado"));
    }

    @Override
    public List<? extends Documento> listar(Long idEntidad) {
        conductor(idEntidad);
        return documentoRepo.findByConductorId(idEntidad);
    }

    @Override
    public Documento obtener(Long idEntidad, Long idDocumento) {
        return documentoRepo.findByIdAndConductorId(idDocumento, idEntidad)
                .orElseThrow(() -> new NotFoundException(
                        "Documento con ID: " + idDocumento + " no encontrado para el conductor " + idEntidad));
    }

    @Override
    public void validarTipoUnico(Long idEntidad, Long idTipoDocumento) {
        if (documentoRepo.existsByConductorIdAndTipoDocumentoIdTipoDocumento(idEntidad, idTipoDocumento)) {
            throw new BadRequestException("El tipo de documento ya está registrado para este conductor");
        }
    }

    @Override
    public Documento crear(Long idEntidad, DocumentoDTORequest request, TipoDocumento tipo, String ruta) {
        return documentoRepo.saveAndFlush(new DocumentoConductor(
                request.nombre().trim(), tipo, ruta, request.fechaEmision(), request.fechaVencimiento(), conductor(idEntidad)));
    }

    @Override
    public Documento guardar(Documento documento) {
        return documentoRepo.saveAndFlush((DocumentoConductor) documento);
    }

    @Override
    public void eliminar(Documento documento) {
        documentoRepo.delete((DocumentoConductor) documento);
        documentoRepo.flush();
    }

    @Override
    public void actualizarHabilitacion(Long idEntidad) {
        Conductor conductor = conductor(idEntidad);
        Set<TipoDocumento> requeridos = tipoRepo.findByRol(RolDocumento.CONDUCTOR);
        conductor.setHabilitado(DocumentoUtils.isHabilitado(requeridos, documentoRepo.findByConductorId(idEntidad)));
        conductorRepository.save(conductor);
    }

    @Override
    public List<Long> listarIdsEntidades() {
        return conductorRepository.findAllIds();
    }
}
