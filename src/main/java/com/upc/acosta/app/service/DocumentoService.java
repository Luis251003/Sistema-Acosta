package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.dto.DocumentoDTOResponse;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.ICamionRepository;
import com.upc.acosta.app.repository.IDocumentoRepository;
import com.upc.acosta.app.repository.ITipoDocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentoService{

//    //REPOSITORIOS
//    private final IDocumentoRepository documentoRepository;
//    private final ICamionRepository camionRepository;
//    private final ITipoDocumentoRepository tipoDocumentoRepository;
//    private final IArchivoService archivoService;
//
//    //METODO CONSTRUCTOR
//    public DocumentoService(
//            IDocumentoRepository documentoRepository,
//            ICamionRepository camionRepository,
//            ITipoDocumentoRepository tipoDocumentoRepository,
//            IArchivoService archivoService
//    ) {
//        this.documentoRepository = documentoRepository;
//        this.camionRepository = camionRepository;
//        this.tipoDocumentoRepository = tipoDocumentoRepository;
//        this.archivoService = archivoService;
//    }
//
//    //METODO DE MAPEO
//    private DocumentoDTOResponse entityToResponse(Documento documento) {
//        return new DocumentoDTOResponse(
//                documento.getId(),
//                documento.getNombre(),
//                documento.getTipoDocumento().getNombre(),
//                documento.getRutaArchivo(),
//                documento.getFechaEmision(),
//                documento.getFechaVencimiento(),
//                documento.obtenerEstadoActual(),
//                documento.getCreatedAt(),
//                documento.getUpdatedAt()
//        );
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<DocumentoDTOResponse> listar(Long idCamion) {
//        Camion camion = camionRepository.findById(idCamion).orElseThrow(() -> new NotFoundException("Camión con ID: " + idCamion + " no encontrado"));
//        return camion.getDocumentos()
//                .stream()
//                .map(this::entityToResponse)
//                .toList();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public byte[] obtenerArchivo(Long idDocumento) {
//        Documento documento = documentoRepository.findById(idDocumento)
//                .orElseThrow(() -> new NotFoundException(
//                        "Documento con ID: " + idDocumento + " no encontrado"
//                ));
//        return archivoService.leerDocumento(documento.getRutaArchivo());
//    }
//
//    @Override
//    @Transactional
//    public DocumentoDTOResponse registrar(
//            Long idCamion,
//            DocumentoDTORequest request,
//            MultipartFile archivo
//    ) {
//        Camion camion = obtenerCamion(idCamion);
//        validarFechas(request);
//        TipoDocumento tipoDocumento = obtenerTipoDocumento(request.idTipoDocumento());
//        validarTipoDocumentoUnico(idCamion, tipoDocumento.get());
//
//        String rutaArchivo = archivoService.subirDocumento(archivo);
//        Documento documento = new Documento(
//                request.nombre().trim(),
//                tipoDocumento,
//                rutaArchivo,
//                request.fechaEmision(),
//                request.fechaVencimiento()
//        );
//
//        Documento guardado = documentoRepository.saveAndFlush(documento);
//        actualizarEstadoCamion(camion);
//        return entityToResponse(guardado);
//    }
//
//    @Override
//    @Transactional
//    public DocumentoCamionDTOResponse actualizar(
//            Long idCamion,
//            Long idDocumento,
//            DocumentoDTORequest request,
//            MultipartFile archivo
//    ) {
//        Camion camion = obtenerCamion(idCamion);
//        Documento documento = obtenerDocumento(idCamion, idDocumento);
//        validarFechas(request);
//        TipoDocumento tipoDocumento = obtenerTipoDocumento(request.idTipoDocumento());
//        validarTipoDocumentoUnico(idCamion, tipoDocumento.getIdTipoDocumento(), idDocumento);
//
//        String rutaAnterior = null;
//        if (archivo != null && !archivo.isEmpty()) {
//            rutaAnterior = documento.getRutaArchivo();
//            documento.setRutaArchivo(archivoService.subirDocumento(archivo));
//        }
//        documento.setNombre(request.nombre().trim());
//        documento.setTipoDocumentoCamion(tipoDocumento);
//        documento.setFechaEmision(request.fechaEmision());
//        documento.setFechaVencimiento(request.fechaVencimiento());
//        documento.actualizarEstado();
//
//        Documento actualizado = documentoRepository.saveAndFlush(documento);
//        if (rutaAnterior != null) {
//            archivoService.eliminarDocumento(rutaAnterior);
//        }
//        actualizarEstadoCamion(camion);
//
//        return entityToResponse(actualizado);
//    }
//
//    @Override
//    @Transactional
//    public void eliminar(Long idCamion, Long idDocumento) {
//        Camion camion = obtenerCamion(idCamion);
//        Documento documento = obtenerDocumento(idCamion, idDocumento);
//
//        documentoRepository.delete(documento);
//        documentoRepository.flush();
//        archivoService.eliminarDocumento(documento.getRutaArchivo());
//        actualizarEstadoCamion(camion);
//    }
//
//    private Camion obtenerCamion(Long idCamion) {
//        return camionRepository.findById(idCamion)
//                .filter(camion -> Boolean.TRUE.equals(camion.getActivo()))
//                .orElseThrow(() -> new NotFoundException(
//                        "Camión con ID: " + idCamion + " no encontrado"
//                ));
//    }
//
//    private TipoDocumento obtenerTipoDocumento(Long idTipoDocumentoCamion) {
//        return tipoDocumentoRepository.findById(idTipoDocumentoCamion)
//                .orElseThrow(() -> new NotFoundException(
//                        "Tipo de documento de camión con ID: " + idTipoDocumentoCamion + " no encontrado"
//                ));
//    }
//
//    private Documento obtenerDocumento(Long idCamion, Long idDocumento) {
//        return documentoRepository.findByIdAndCamion_Id(idDocumento, idCamion)
//                .orElseThrow(() -> new NotFoundException(
//                        "Documento con ID: " + idDocumento + " no encontrado para el camión " + idCamion
//                ));
//    }
//
//    private void validarFechas(DocumentoDTORequest request) {
//        if (request.fechaEmision() == null || request.fechaVencimiento() == null) {
//            throw new BadRequestException("Las fechas de emisión y vencimiento son obligatorias");
//        }
//        if (request.fechaEmision().isAfter(LocalDate.now())) {
//            throw new BadRequestException("La fecha de emisión no puede ser futura");
//        }
//        if (request.fechaVencimiento().isBefore(request.fechaEmision())) {
//            throw new BadRequestException(
//                    "La fecha de vencimiento no puede ser anterior a la fecha de emisión"
//            );
//        }
//    }
//
//    private void actualizarEstadoCamion(Camion camion) {
//        List<TipoDocumentoCamion> tiposObligatorios = tipoDocumentoRepository.findAll();
//        List<Documento> documentos = documentoRepository.findAllByCamion_Id(camion.getId());
//        LocalDate fechaConsulta = LocalDate.now();
//
//        boolean habilitado = !tiposObligatorios.isEmpty() && tiposObligatorios.stream().allMatch(tipo ->
//                documentos.stream().anyMatch(documento ->
//                        documento.getTipoDocumentoCamion().getIdTipoDocumentoCamion()
//                                .equals(tipo.getIdTipoDocumentoCamion())
//                                && documento.getFechaVencimiento() != null
//                                && !documento.getFechaVencimiento().isBefore(fechaConsulta)
//                )
//        );
//
//        camion.setEstado(habilitado ? EstadoCamion.HABILITADO : EstadoCamion.INHABILITADO);
//        camionRepository.save(camion);
//    }
}
