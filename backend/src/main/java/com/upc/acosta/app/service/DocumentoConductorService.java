package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoConductorDTORequest;
import com.upc.acosta.app.dto.DocumentoConductorDTOResponse;
import com.upc.acosta.app.entity.Conductor;
import com.upc.acosta.app.entity.DocumentoConductor;
import com.upc.acosta.app.entity.TipoDocumentoConductor;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.IConductorRepository;
import com.upc.acosta.app.repository.IDocumentoConductorRepository;
import com.upc.acosta.app.repository.ITipoDocumentoConductorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentoConductorService implements IDocumentoConductorService {

    private final IDocumentoConductorRepository documentoRepository;
    private final IConductorRepository conductorRepository;
    private final ITipoDocumentoConductorRepository tipoDocumentoRepository;
    private final IArchivoService archivoService;

    public DocumentoConductorService(
            IDocumentoConductorRepository documentoRepository,
            IConductorRepository conductorRepository,
            ITipoDocumentoConductorRepository tipoDocumentoRepository,
            IArchivoService archivoService
    ) {
        this.documentoRepository = documentoRepository;
        this.conductorRepository = conductorRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.archivoService = archivoService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoDocumentoConductor> listarTipos() {
        return tipoDocumentoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoConductorDTOResponse> listar(Long idConductor) {
        obtenerConductor(idConductor);
        return documentoRepository.findAllByConductorIdOrderByCreatedAtDesc(idConductor)
                .stream()
                .map(this::entityToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoConductorDTOResponse buscar(Long idConductor, Long idDocumento) {
        return entityToResponse(obtenerDocumento(idConductor, idDocumento));
    }

    @Override
    @Transactional
    public DocumentoConductorDTOResponse registrar(
            Long idConductor,
            DocumentoConductorDTORequest request,
            MultipartFile archivo
    ) {
        Conductor conductor = obtenerConductor(idConductor);
        validarFechas(request);
        TipoDocumentoConductor tipoDocumento = obtenerTipoDocumento(request.idTipoDocumentoConductor());
        validarTipoDocumentoUnico(idConductor, tipoDocumento.getIdTipoDocumentoConductor());
        String rutaArchivo = archivoService.subirDocumento(archivo);

        DocumentoConductor documento = new DocumentoConductor(
                conductor,
                request.nombre().trim(),
                tipoDocumento,
                rutaArchivo,
                request.fechaEmision(),
                request.fechaVencimiento()
        );

        //ACTUALIZAMOS EL ESTADO DEL CONDUCTOR DESPUES DE REGISTRAR EL DOCUMENTO
        actualizarEstadoConductor(idConductor);

        return entityToResponse(documentoRepository.save(documento));
    }

    @Override
    @Transactional
    public DocumentoConductorDTOResponse actualizar(
            Long idConductor,
            Long idDocumento,
            DocumentoConductorDTORequest request,
            MultipartFile archivo
    ) {
        DocumentoConductor documento = obtenerDocumento(idConductor, idDocumento);
        validarFechas(request);
        TipoDocumentoConductor tipoDocumento = obtenerTipoDocumento(request.idTipoDocumentoConductor());
        validarTipoDocumentoUnico(idConductor, tipoDocumento.getIdTipoDocumentoConductor(), idDocumento);

        String rutaAnterior = null;
        if (archivo != null && !archivo.isEmpty()) {
            rutaAnterior = documento.getRutaArchivo();
            documento.setRutaArchivo(archivoService.subirDocumento(archivo));
        }
        documento.setNombre(request.nombre().trim());
        documento.setTipoDocumentoConductor(tipoDocumento);
        documento.setFechaEmision(request.fechaEmision());
        documento.setFechaVencimiento(request.fechaVencimiento());

        DocumentoConductor actualizado = documentoRepository.saveAndFlush(documento);
        if (rutaAnterior != null) {
            archivoService.eliminarDocumento(rutaAnterior);
        }

        //ACTUALIZAMOS EL ESTADO DEL CONDUCTOR DESPUES DE REGISTRAR EL DOCUMENTO
        actualizarEstadoConductor(idConductor);

        return entityToResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long idConductor, Long idDocumento) {
        DocumentoConductor documento = obtenerDocumento(idConductor, idDocumento);
        documentoRepository.delete(documento);
        documentoRepository.flush();
        archivoService.eliminarDocumento(documento.getRutaArchivo());

        //ACTUALIZAMOS EL ESTADO DEL CONDUCTOR DESPUES DE REGISTRAR EL DOCUMENTO
        actualizarEstadoConductor(idConductor);
    }

    //METODOS FUNCIONALES
    private Conductor obtenerConductor(Long idConductor) {
        return conductorRepository.findById(idConductor)
                .orElseThrow(() -> new NotFoundException(
                        "Conductor con ID: " + idConductor + " no encontrado"
                ));
    }

    private DocumentoConductor obtenerDocumento(Long idConductor, Long idDocumento) {
        return documentoRepository.findByIdAndConductorId(idDocumento, idConductor)
                .orElseThrow(() -> new NotFoundException(
                        "Documento con ID: " + idDocumento + " no encontrado para el conductor " + idConductor
                ));
    }

    private TipoDocumentoConductor obtenerTipoDocumento(Long idTipoDocumentoConductor) {
        return tipoDocumentoRepository.findById(idTipoDocumentoConductor)
                .orElseThrow(() -> new NotFoundException(
                        "Tipo de documento con ID: " + idTipoDocumentoConductor + " no encontrado"
                ));
    }

    private void validarTipoDocumentoUnico(Long idConductor, Long idTipoDocumentoConductor) {
        if (documentoRepository.existsByConductor_IdAndTipoDocumentoConductor_IdTipoDocumentoConductor(
                idConductor,
                idTipoDocumentoConductor
        )) {
            throw new DataExistException("El conductor ya tiene registrado un documento de este tipo");
        }
    }

    private void validarTipoDocumentoUnico(
            Long idConductor,
            Long idTipoDocumentoConductor,
            Long idDocumento
    ) {
        if (documentoRepository.existsByConductor_IdAndTipoDocumentoConductor_IdTipoDocumentoConductorAndIdNot(
                idConductor,
                idTipoDocumentoConductor,
                idDocumento
        )) {
            throw new DataExistException("El conductor ya tiene registrado un documento de este tipo");
        }
    }

    private void validarFechas(DocumentoConductorDTORequest request) {
        if (request.fechaEmision() == null || request.fechaVencimiento() == null) {
            throw new BadRequestException("Las fechas de emisión y vencimiento son obligatorias");
        }
        if (request.fechaEmision().isAfter(LocalDate.now())) {
            throw new BadRequestException("La fecha de emisión no puede ser futura");
        }
        if (request.fechaVencimiento().isBefore(request.fechaEmision())) {
            throw new BadRequestException("La fecha de vencimiento no puede ser anterior a la fecha de emisión");
        }
    }

    private DocumentoConductorDTOResponse entityToResponse(DocumentoConductor documento) {
        return new DocumentoConductorDTOResponse(
                documento.getId(),
                documento.getConductor().getId(),
                documento.getNombre(),
                documento.getTipoDocumentoConductor().getNombre(),
                documento.getRutaArchivo(),
                documento.getFechaEmision(),
                documento.getFechaVencimiento(),
                documento.obtenerEstadoActual(),
                documento.getCreatedAt(),
                documento.getUpdatedAt()
        );
    }

    @Transactional
    private void actualizarEstadoConductor(Long idConductor){
        //Lista de tipos de documentos obligatorios
        List<TipoDocumentoConductor> tiposObligatorios = tipoDocumentoRepository.findAll();
        List<DocumentoConductor> documentos = documentoRepository.findAllByConductorId(idConductor);
        LocalDate fechaConsulta = LocalDate.now();

        Boolean response = !tiposObligatorios.isEmpty() && tiposObligatorios.stream().allMatch(tipo ->
                documentos.stream().anyMatch(documento ->
                        documento.getTipoDocumentoConductor().equals(tipo)
                                && documento.getFechaVencimiento() != null
                                && !documento.getFechaVencimiento()
                                .isBefore(fechaConsulta)
                )
        );
        conductorRepository.actualizarEstadoPorId(idConductor, response);
    }
}
