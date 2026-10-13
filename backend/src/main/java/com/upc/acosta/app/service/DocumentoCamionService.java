package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoCamionDTORequest;
import com.upc.acosta.app.dto.DocumentoCamionDTOResponse;
import com.upc.acosta.app.entity.Camion;
import com.upc.acosta.app.entity.DocumentoCamion;
import com.upc.acosta.app.entity.EstadoCamion;
import com.upc.acosta.app.entity.TipoDocumentoCamion;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.ICamionRepository;
import com.upc.acosta.app.repository.IDocumentoCamionRepository;
import com.upc.acosta.app.repository.ITipoDocumentoCamionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentoCamionService implements IDocumentoCamionService {

    private final IDocumentoCamionRepository documentoRepository;
    private final ICamionRepository camionRepository;
    private final ITipoDocumentoCamionRepository tipoDocumentoRepository;
    private final IArchivoService archivoService;

    public DocumentoCamionService(
            IDocumentoCamionRepository documentoRepository,
            ICamionRepository camionRepository,
            ITipoDocumentoCamionRepository tipoDocumentoRepository,
            IArchivoService archivoService
    ) {
        this.documentoRepository = documentoRepository;
        this.camionRepository = camionRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.archivoService = archivoService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoCamionDTOResponse> listar(Long idCamion) {
        obtenerCamion(idCamion);
        return documentoRepository.findAllByCamion_IdOrderByCreatedAtDesc(idCamion)
                .stream()
                .map(this::entityToResponse)
                .toList();
    }

    @Override
    @Transactional
    public DocumentoCamionDTOResponse registrar(
            Long idCamion,
            DocumentoCamionDTORequest request,
            MultipartFile archivo
    ) {
        Camion camion = obtenerCamion(idCamion);
        validarFechas(request);
        TipoDocumentoCamion tipoDocumento = obtenerTipoDocumento(request.idTipoDocumentoCamion());
        validarTipoDocumentoUnico(idCamion, tipoDocumento.getIdTipoDocumentoCamion());

        String rutaArchivo = archivoService.subirDocumento(archivo);
        DocumentoCamion documento = new DocumentoCamion(
                camion,
                request.nombre().trim(),
                tipoDocumento,
                rutaArchivo,
                request.fechaEmision(),
                request.fechaVencimiento()
        );

        DocumentoCamion guardado = documentoRepository.saveAndFlush(documento);
        actualizarEstadoCamion(camion);
        return entityToResponse(guardado);
    }

    @Override
    @Transactional
    public DocumentoCamionDTOResponse actualizar(
            Long idCamion,
            Long idDocumento,
            DocumentoCamionDTORequest request,
            MultipartFile archivo
    ) {
        Camion camion = obtenerCamion(idCamion);
        DocumentoCamion documento = obtenerDocumento(idCamion, idDocumento);
        validarFechas(request);
        TipoDocumentoCamion tipoDocumento = obtenerTipoDocumento(request.idTipoDocumentoCamion());
        validarTipoDocumentoUnico(idCamion, tipoDocumento.getIdTipoDocumentoCamion(), idDocumento);

        String rutaAnterior = null;
        if (archivo != null && !archivo.isEmpty()) {
            rutaAnterior = documento.getRutaArchivo();
            documento.setRutaArchivo(archivoService.subirDocumento(archivo));
        }
        documento.setNombre(request.nombre().trim());
        documento.setTipoDocumentoCamion(tipoDocumento);
        documento.setFechaEmision(request.fechaEmision());
        documento.setFechaVencimiento(request.fechaVencimiento());
        documento.actualizarEstado();

        DocumentoCamion actualizado = documentoRepository.saveAndFlush(documento);
        if (rutaAnterior != null) {
            archivoService.eliminarDocumento(rutaAnterior);
        }
        actualizarEstadoCamion(camion);

        return entityToResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long idCamion, Long idDocumento) {
        Camion camion = obtenerCamion(idCamion);
        DocumentoCamion documento = obtenerDocumento(idCamion, idDocumento);

        documentoRepository.delete(documento);
        documentoRepository.flush();
        archivoService.eliminarDocumento(documento.getRutaArchivo());
        actualizarEstadoCamion(camion);
    }

    private Camion obtenerCamion(Long idCamion) {
        return camionRepository.findById(idCamion)
                .filter(camion -> Boolean.TRUE.equals(camion.getActivo()))
                .orElseThrow(() -> new NotFoundException(
                        "Camión con ID: " + idCamion + " no encontrado"
                ));
    }

    private TipoDocumentoCamion obtenerTipoDocumento(Long idTipoDocumentoCamion) {
        return tipoDocumentoRepository.findById(idTipoDocumentoCamion)
                .orElseThrow(() -> new NotFoundException(
                        "Tipo de documento de camión con ID: " + idTipoDocumentoCamion + " no encontrado"
                ));
    }

    private void validarTipoDocumentoUnico(Long idCamion, Long idTipoDocumentoCamion) {
        if (documentoRepository.existsByCamion_IdAndTipoDocumentoCamion_IdTipoDocumentoCamion(
                idCamion,
                idTipoDocumentoCamion
        )) {
            throw new DataExistException("El camión ya tiene registrado un documento de este tipo");
        }
    }

    private void validarTipoDocumentoUnico(Long idCamion, Long idTipoDocumentoCamion, Long idDocumento) {
        if (documentoRepository.existsByCamion_IdAndTipoDocumentoCamion_IdTipoDocumentoCamionAndIdNot(
                idCamion,
                idTipoDocumentoCamion,
                idDocumento
        )) {
            throw new DataExistException("El camión ya tiene registrado un documento de este tipo");
        }
    }

    private DocumentoCamion obtenerDocumento(Long idCamion, Long idDocumento) {
        return documentoRepository.findByIdAndCamion_Id(idDocumento, idCamion)
                .orElseThrow(() -> new NotFoundException(
                        "Documento con ID: " + idDocumento + " no encontrado para el camión " + idCamion
                ));
    }

    private void validarFechas(DocumentoCamionDTORequest request) {
        if (request.fechaEmision() == null || request.fechaVencimiento() == null) {
            throw new BadRequestException("Las fechas de emisión y vencimiento son obligatorias");
        }
        if (request.fechaEmision().isAfter(LocalDate.now())) {
            throw new BadRequestException("La fecha de emisión no puede ser futura");
        }
        if (request.fechaVencimiento().isBefore(request.fechaEmision())) {
            throw new BadRequestException(
                    "La fecha de vencimiento no puede ser anterior a la fecha de emisión"
            );
        }
    }

    private DocumentoCamionDTOResponse entityToResponse(DocumentoCamion documento) {
        return new DocumentoCamionDTOResponse(
                documento.getId(),
                documento.getCamion().getId(),
                documento.getNombre(),
                documento.getTipoDocumentoCamion().getNombre(),
                documento.getRutaArchivo(),
                documento.getFechaEmision(),
                documento.getFechaVencimiento(),
                documento.obtenerEstadoActual(),
                documento.getCreatedAt(),
                documento.getUpdatedAt()
        );
    }

    private void actualizarEstadoCamion(Camion camion) {
        List<TipoDocumentoCamion> tiposObligatorios = tipoDocumentoRepository.findAll();
        List<DocumentoCamion> documentos = documentoRepository.findAllByCamion_Id(camion.getId());
        LocalDate fechaConsulta = LocalDate.now();

        boolean habilitado = !tiposObligatorios.isEmpty() && tiposObligatorios.stream().allMatch(tipo ->
                documentos.stream().anyMatch(documento ->
                        documento.getTipoDocumentoCamion().getIdTipoDocumentoCamion()
                                .equals(tipo.getIdTipoDocumentoCamion())
                                && documento.getFechaVencimiento() != null
                                && !documento.getFechaVencimiento().isBefore(fechaConsulta)
                )
        );

        camion.setEstado(habilitado ? EstadoCamion.HABILITADO : EstadoCamion.INHABILITADO);
        camionRepository.save(camion);
    }
}
