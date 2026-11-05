package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.dto.DocumentoDTOResponse;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.entity.tipos.EstadoCamion;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.upc.acosta.app.entity.Documento;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class DocumentoService implements IDocumentoService {

    //REPOSITORIOS
    private final IDocumentosCamionRepository documentosCamionRepository;
    private final IDocumentosConductorRepository documentosConductorRepository;
    private final ICamionRepository camionRepository;
    private final IConductorRepository conductorRepository;
    private final ITipoDocumentoRepository tipoDocumentoRepository;
    private final IArchivoService archivoService;

    //METODO CONSTRUCTOR
    public DocumentoService(
            IDocumentosCamionRepository documentosCamionRepository,
            IDocumentosConductorRepository documentosConductorRepository,
            ICamionRepository camionRepository,
            IConductorRepository conductorRepository,
            ITipoDocumentoRepository tipoDocumentoRepository,
            IArchivoService archivoService
    ) {
        this.documentosCamionRepository = documentosCamionRepository;
        this.documentosConductorRepository = documentosConductorRepository;
        this.camionRepository = camionRepository;
        this.conductorRepository = conductorRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.archivoService = archivoService;
    }

    //METODO DE MAPEO
    private DocumentoDTOResponse entityToResponse(Documento documento) {
        return new DocumentoDTOResponse(
                documento.getId(),
                documento.getNombre(),
                documento.getTipoDocumento().getNombre(),
                documento.getRutaArchivo(),
                documento.getFechaEmision(),
                documento.getFechaVencimiento(),
                documento.obtenerEstadoActual(),
                documento.getCreatedAt(),
                documento.getUpdatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoDTOResponse> listarDocumentos(Long id, RolDocumento rol) {

        return switch (rol) {
            case RolDocumento.CAMION -> documentosCamionRepository
                    .findByCamionId(id)
                    .orElseThrow(() -> new NotFoundException("Camión con ID: " + id + " no encontrado"))
                    .stream().map(this::entityToResponse).toList();
            case RolDocumento.CONDUCTOR -> documentosConductorRepository
                    .findByConductorId(id)
                    .orElseThrow(() -> new NotFoundException("Conductor con ID: " + id + " no encontrado"))
                    .stream().map(this::entityToResponse).toList();
            default -> throw new BadRequestException("Rol no válido");
        };
    }

    public String obtenerArchivoURL(Long idEntidad, Long idDocumento, RolDocumento rol) {

        Documento documento;
        String key = "";

        switch (rol) {
            case RolDocumento.CAMION:
                documento = documentosCamionRepository.findByIdAndCamionId(idDocumento, idEntidad).orElseThrow(() -> new NotFoundException("Documento con ID " + idDocumento + " no encontrado para el camión con ID " + idEntidad));
                key = documento.getRutaArchivo();
                return archivoService.obtenerUrlTemporal(key).toString();
            case RolDocumento.CONDUCTOR:
                documento = documentosConductorRepository.findByIdAndConductorId(idDocumento, idEntidad).orElseThrow(() -> new NotFoundException("Documento con ID " + idDocumento + " no encontrado para el conductor con ID " + idEntidad));
                key = documento.getRutaArchivo();
                return archivoService.obtenerUrlTemporal(key).toString();
        }

        return archivoService.obtenerUrlTemporal(key).toString();
    }

    @Override
    public DocumentoDTOResponse registrarDocumento(
            Long id,
            DocumentoDTORequest request,
            MultipartFile archivo,
            RolDocumento rol
    ) {

        //VALIDAMOS LAS FECHAS DE EMISION Y VENCIMIENTO
        validarFechas(request);
        //OBTENEMOS EL TIPO DE DOCUMENTO
        TipoDocumento tipoDocumento = obtenerTipoDocumento(request.idTipoDocumento());
        //VALIDAMOS QUE EL TIPO DE DOCUMENTO CORRESPONDA AL ROL ASIGNADO
        if (tipoDocumento.getRol() != rol) {
            throw new BadRequestException("El tipo de documento no corresponde al rol asignado");
        }

        //ELEGIMOS A QUE TIPO DE ROL PERTENECE EL DOCUMENTO
        return switch (rol) {
            case RolDocumento.CAMION -> registrarDocumentoCamion(id, request, tipoDocumento, archivo);
            case RolDocumento.CONDUCTOR -> registrarDocumentoConductor(id, request, tipoDocumento, archivo);
            default -> throw new BadRequestException("Rol no válido");
        };
    }

    @Override
    @Transactional
    public DocumentoDTOResponse actualizarDocumento(
            Long idEntidad,
            Long idDocumento,
            DocumentoDTORequest request,
            MultipartFile archivo,
            RolDocumento rol
    ) {
        //VALIDAMOS LAS FECHAS DE EMISION Y VENCIMIENTO
        validarFechas(request);
        //OBTENEMOS EL TIPO DE DOCUMENTO
        TipoDocumento tipoDocumento = obtenerTipoDocumento(request.idTipoDocumento());
        //VALIDAMOS QUE EL TIPO DE DOCUMENTO CORRESPONDA AL ROL ASIGNADO
        if (tipoDocumento.getRol() != rol) {
            throw new BadRequestException("El tipo de documento no corresponde al rol asignado");
        }

        //ELEGIMOS A QUE TIPO DE ROL PERTENECE EL DOCUMENTO
        return switch (rol) {
            case RolDocumento.CAMION -> actualizarDocumentoCamion(idEntidad, idDocumento, request, archivo);
            case RolDocumento.CONDUCTOR -> actualizarDocumentoConductor(idEntidad, idDocumento, request, archivo);
            default -> throw new BadRequestException("Rol no válido");
        };
    }

    @Transactional
    private DocumentoDTOResponse actualizarDocumentoConductor(Long idConductor, Long idDocumento, DocumentoDTORequest request, MultipartFile archivo) {
        //OBTENEMOS AL CONDUCTOR
        Conductor conductor = obtenerConductor(idConductor);
        //OBTENEMOS EL DOCUMENTO DEL CONDUCTOR
        DocumentoConductor documento = obtenerDocumentoConductor(idConductor, idDocumento);

        //GUARDAMOS LA RUTA ANTERIOR
        String rutaAnterior = null;
        if (archivo != null && !archivo.isEmpty()) {
            rutaAnterior = documento.getRutaArchivo();
            //SUBIMOS EL ARCHIVO NUEVO
            documento.setRutaArchivo(archivoService.subirDocumento(archivo,"conductor"));
        }

        //ACTUALIZAMOS LOS DATOS DEL ARCHIVO
        documento.setNombre(request.nombre().trim());
        documento.setFechaEmision(request.fechaEmision());
        documento.setFechaVencimiento(request.fechaVencimiento());
        documento.actualizarEstado();

        //REGISTRAMOS EL DOCUMENTO DEL CONDUCTOR
        Documento actualizado = documentosConductorRepository.saveAndFlush(documento);
        //ELIMINAMOS EL DOCUMENTO
        if (rutaAnterior != null) {
            archivoService.eliminarArchivo(rutaAnterior);
        }
        //ACTUALIZAMOS EL ESTADO DEL CONDUCTOR
        actualizarEstadoConductor(conductor);
        //DEVOLVEMOS LA INFORMACION DEL DOCUMENTO
        return entityToResponse(actualizado);
    }

    @Transactional
    private DocumentoDTOResponse actualizarDocumentoCamion(Long idCamion, Long idDocumento, DocumentoDTORequest request, MultipartFile archivo) {
        //OBTENEMOS AL CAMION
        Camion camion = obtenerCamion(idCamion);
        //OBTENEMOS EL DOCUMENTO DEL CAMION
        DocumentoCamion documento = obtenerDocumentoCamion(idCamion, idDocumento);

        //GUARDAMOS LA RUTA ANTERIOR
        String rutaAnterior = null;
        if (archivo != null && !archivo.isEmpty()) {
            rutaAnterior = documento.getRutaArchivo();
            //REGISTRAMOS EL DOCUMENTO NUEVO
            documento.setRutaArchivo(archivoService.subirDocumento(archivo,"camion"));
        }
        //ACTUALIZAMOS LOS DATOS DEL DOCUMENTO
        documento.setNombre(request.nombre().trim());
        documento.setFechaEmision(request.fechaEmision());
        documento.setFechaVencimiento(request.fechaVencimiento());
        documento.actualizarEstado();
        //GUARDAMOS LOS CAMBIOS EN LA DB
        Documento actualizado = documentosCamionRepository.saveAndFlush(documento);
        //ELIMINAMOS EL DOCUMENTO ANTERIOR
        if (rutaAnterior != null) {
            archivoService.eliminarArchivo(rutaAnterior);
        }
        //ACTUALIZAMOS EL ESTADO DEL CAMION
        actualizarEstadoCamion(camion);
        //DEVOLVEMOS LOS DATOS DEL DOCUMENTO NUEVO
        return entityToResponse(actualizado);
    }

    @Override
    public void eliminar(Long idEntidad, Long idDocumento, RolDocumento rolDocumento) {
        switch (rolDocumento) {
            case RolDocumento.CAMION -> eliminarDocumentoCamion(idEntidad, idDocumento);
            case RolDocumento.CONDUCTOR -> eliminarDocumentoConductor(idEntidad, idDocumento);
            default -> throw new BadRequestException("Rol no válido");
        }
    }

    @Transactional
    private void eliminarDocumentoConductor(Long idEntidad, Long idDocumento) {
        //OBTENEMOS LOS ATRIBUTOS PRINCIPALES
        Conductor conductor = obtenerConductor(idEntidad);
        DocumentoConductor documento = obtenerDocumentoConductor(idEntidad, idDocumento);
        String rutaArchivo = documento.getRutaArchivo();
        //ELIMINAMOS EL ARCHIVO DEL DB
        documentosConductorRepository.delete(documento);
        documentosConductorRepository.flush();
        //ELIMINAMOS EL ARCHIVO DE LA NUBE
        archivoService.eliminarArchivo(rutaArchivo);
        //ACTUALIZAMOS EL ESTADO DEL CONDUCTOR
        actualizarEstadoConductor(conductor);
    }

    @Transactional
    private void eliminarDocumentoCamion(Long idCamion, Long idDocumento){
        //OBTENEMOS LOS ATRIBUTOS PRINCIPALES
        Camion camion = obtenerCamion(idCamion);
        DocumentoCamion documento = obtenerDocumentoCamion(idCamion, idDocumento);
        String rutaArchivo = documento.getRutaArchivo();
        //ELIMINAMOS EL ARCHIVO DEL DB
        documentosCamionRepository.delete(documento);
        documentosCamionRepository.flush();
        //ELIMINAMOS EL ARCHIVO DE LA NUBE
        archivoService.eliminarArchivo(rutaArchivo);
        //ACTUALIZAMOS EL ESTADO DEL CAMION
        actualizarEstadoCamion(camion);
    }

    @Transactional
    private DocumentoDTOResponse registrarDocumentoConductor(Long id, DocumentoDTORequest request, TipoDocumento tipoDocumento, MultipartFile archivo) {
        Conductor conductor = obtenerConductor(id);
        validarTipoDocumentoUnicoConductor(id, tipoDocumento.getIdTipoDocumento());
        String rutaArchivo = archivoService.subirDocumento(archivo,"conductor");
        //CREAMOS EL DOCUMENTO CONDUCTOR
        DocumentoConductor documentoConductor = new DocumentoConductor(
                request.nombre().trim(),
                tipoDocumento,
                rutaArchivo,
                request.fechaEmision(),
                request.fechaVencimiento(),
                conductor
        );
        Documento guardadoConductor = documentosConductorRepository.saveAndFlush(documentoConductor);
        actualizarEstadoConductor(conductor);
        return entityToResponse(guardadoConductor);
    }

    @Transactional
    private DocumentoDTOResponse registrarDocumentoCamion(Long id,DocumentoDTORequest request,TipoDocumento tipoDocumento,MultipartFile archivo){
        Camion camion = obtenerCamion(id);
        validarTipoDocumentoUnicoCamion(id, tipoDocumento.getIdTipoDocumento());
        String rutaArchivo = archivoService.subirDocumento(archivo,"camion");
        //CREAMOS EL DOCUMENTO CAMION
        DocumentoCamion documentoCamion = new DocumentoCamion(
                request.nombre().trim(),
                tipoDocumento,
                rutaArchivo,
                request.fechaEmision(),
                request.fechaVencimiento(),
                camion
        );
        Documento guardadoCamion = documentosCamionRepository.saveAndFlush(documentoCamion);
        actualizarEstadoCamion(camion);
        return entityToResponse(guardadoCamion);
    }

    @Transactional(readOnly = true)
    private Camion obtenerCamion(Long idCamion) {
        return camionRepository.findById(idCamion)
                .filter(camion -> Boolean.TRUE.equals(camion.getActivo()))
                .orElseThrow(() -> new NotFoundException(
                        "Camión con ID: " + idCamion + " no encontrado"
                ));
    }

    @Transactional(readOnly = true)
    private Conductor obtenerConductor(Long idConductor){
        return conductorRepository.findById(idConductor)
                .orElseThrow(() -> new NotFoundException(
                        "Conductor con ID: " + idConductor + " no encontrado"
                ));
    }

    @Transactional(readOnly = true)
    private TipoDocumento obtenerTipoDocumento(Long idTipoDocumentoCamion) {
        return tipoDocumentoRepository.findById(idTipoDocumentoCamion)
                .orElseThrow(() -> new NotFoundException(
                        "Tipo de documento de camión con ID: " + idTipoDocumentoCamion + " no encontrado"
                ));
    }

    @Transactional(readOnly = true)
    private DocumentoCamion obtenerDocumentoCamion(Long idCamion, Long idDocumento) {
        return documentosCamionRepository.findByIdAndCamionId(idDocumento, idCamion)
                .orElseThrow(() -> new NotFoundException(
                        "Documento con ID: " + idDocumento + " no encontrado para el camión " + idCamion
                ));
    }

    @Transactional(readOnly = true)
    private DocumentoConductor obtenerDocumentoConductor(Long idConductor, Long idDocumento) {
        return documentosConductorRepository.findByIdAndConductorId(idDocumento, idConductor)
                .orElseThrow(() -> new NotFoundException(
                        "Documento con ID: " + idDocumento + " no encontrado para el conductor " + idConductor
                ));
    }

    private void validarFechas(DocumentoDTORequest request) {
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

    private void validarTipoDocumentoUnicoCamion(Long idCamion, Long idTipoDocumento) {
        List<DocumentoCamion> documentos = documentosCamionRepository.findByCamionId(idCamion).orElseThrow(()-> new NotFoundException("Camión con ID " + idCamion + " no encontrado"));
        if (documentos.stream().anyMatch(doc -> doc.getTipoDocumento().getIdTipoDocumento().equals(idTipoDocumento))) {
            throw new BadRequestException("El tipo de documento ya está registrado para este camión");
        }
    }

    private void validarTipoDocumentoUnicoConductor(Long idConductor, Long idTipoDocumento) {
        List<DocumentoConductor> documentos = documentosConductorRepository.findByConductorId(idConductor).orElseThrow(()-> new NotFoundException("Conductor con ID " + idConductor + " no encontrado"));
        if (documentos.stream().anyMatch(doc -> doc.getTipoDocumento().getIdTipoDocumento().equals(idTipoDocumento))) {
            throw new BadRequestException("El tipo de documento ya está registrado para este conductor");
        }
    }

    @Transactional
    private void actualizarEstadoCamion(Camion camion) {
        Set<TipoDocumento> tipoDocumentosCamion = tipoDocumentoRepository.findByRol(RolDocumento.CAMION);
        List<DocumentoCamion> documentos = documentosCamionRepository.findByCamionId(camion.getId()).orElseThrow(()-> new NotFoundException("Camión con ID " + camion.getId() + " no encontrado"));

        camion.setEstado(isHabilitado(tipoDocumentosCamion, documentos) ? EstadoCamion.HABILITADO : EstadoCamion.INHABILITADO);
        camionRepository.save(camion);
    }

    @Transactional
    private void actualizarEstadoConductor(Conductor conductor){
        Set<TipoDocumento> tipoDocumentosConductor = tipoDocumentoRepository.findByRol(RolDocumento.CONDUCTOR);
        List<DocumentoConductor> documentos = documentosConductorRepository.findByConductorId(conductor.getId()).orElseThrow(()-> new NotFoundException("Conductor con ID " + conductor.getId() + " no encontrado"));
        conductor.setEstado(isHabilitado(tipoDocumentosConductor, documentos));
        conductorRepository.save(conductor);
    }

    private boolean isHabilitado(Set<TipoDocumento> tipoDocumento, List<? extends Documento> documentos) {
        LocalDate fechaConsulta = LocalDate.now();

        return !tipoDocumento.isEmpty() && tipoDocumento.stream().allMatch(tipo ->
                documentos.stream().anyMatch(documento ->
                        documento.getTipoDocumento().getIdTipoDocumento()
                                    .equals(tipo.getIdTipoDocumento())
                                && documento.getFechaVencimiento() != null
                                && !documento.getFechaVencimiento().isBefore(fechaConsulta)
                )
        );
    }
}
