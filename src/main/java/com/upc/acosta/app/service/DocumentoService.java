package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.DocumentoDTORequest;
import com.upc.acosta.app.dto.DocumentoDTOResponse;
import com.upc.acosta.app.dto.ResultadoActualizacion;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.*;
import com.upc.acosta.app.service.interfaces.IArchivoService;
import com.upc.acosta.app.service.interfaces.IDocumentoHandler;
import com.upc.acosta.app.service.interfaces.IDocumentoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.upc.acosta.app.entity.Documento;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentoService implements IDocumentoService {

    //REPOSITORIOS
    private final Map<RolDocumento, IDocumentoHandler> handlers;
    private final ITipoDocumentoRepository tipoDocumentoRepository;
    private final IArchivoService archivoService;
    private final TransactionTemplate transactionTemplate;

    public DocumentoService(List<IDocumentoHandler> handlers,
                            ITipoDocumentoRepository tipoDocumentoRepository,
                            IArchivoService archivoService, TransactionTemplate transactionTemplate) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(IDocumentoHandler::rol, Function.identity()));
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.archivoService = archivoService;
        this.transactionTemplate = transactionTemplate;
    }
    // METODO DE MAPEO
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

    // ::::::::::::::::::::::::::::::: OBTENER INTERFACE SEGUN ROL :::::::::::::::::::::::::::::::

    private IDocumentoHandler handler(RolDocumento rol) {
        IDocumentoHandler h = handlers.get(rol);
        if (h == null) throw new BadRequestException("Rol no válido");
        return h;
    }

    // ::::::::::::::::::::::::::::::: METODOS DE VALIDACION :::::::::::::::::::::::::::::::

    private TipoDocumento validarSolicitud(DocumentoDTORequest request, RolDocumento rol) {
        validarFechas(request);
        TipoDocumento tipo = tipoDocumentoRepository.findById(request.idTipoDocumento())
                .orElseThrow(() -> new NotFoundException(
                        "Tipo de documento con ID: " + request.idTipoDocumento() + " no encontrado"));
        if (tipo.getRol() != rol) {
            throw new BadRequestException("El tipo de documento no corresponde al rol asignado");
        }
        return tipo;
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

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoDTOResponse> listarDocumentos(Long id, RolDocumento rol) {
        //Actualizamos el estado de habilitacion antes de listar
        this.actualizarEstadosDocumentos(id,rol);
        //Devolvemos la lista de documentos actualizada
        return handler(rol).listar(id).stream().map(this::entityToResponse).toList();
    }

    @Transactional(readOnly = true)
    public String obtenerArchivoURL(Long idEntidad, Long idDocumento, RolDocumento rol) {
        Documento documento = handler(rol).obtener(idEntidad, idDocumento);
        return archivoService.obtenerUrlTemporal(documento.getRutaArchivo()).toString();
    }

    @Override
    @Transactional
    public DocumentoDTOResponse registrarDocumento(Long id, DocumentoDTORequest request,
                                                   MultipartFile archivo, RolDocumento rol) {
        IDocumentoHandler h = handler(rol);
        TipoDocumento tipo = validarSolicitud(request, rol);
        if (archivo == null || archivo.isEmpty()) {
            throw new BadRequestException("El archivo es obligatorio");
        }
        h.validarTipoUnico(id, tipo.getIdTipoDocumento());

        String ruta = archivoService.subirDocumento(archivo, h.carpeta());
        Documento guardado = h.crear(id, request, tipo, ruta);
        h.actualizarHabilitacion(id);
        return entityToResponse(guardado);
    }

    @Override
    @Transactional
    public DocumentoDTOResponse actualizarDocumento(Long idEntidad, Long idDocumento,
                                                    DocumentoDTORequest request,
                                                    MultipartFile archivo, RolDocumento rol) {
        IDocumentoHandler h = handler(rol);
        validarSolicitud(request, rol);
        Documento documento = h.obtener(idEntidad, idDocumento);

        String rutaAnterior = null;
        if (archivo != null && !archivo.isEmpty()) {
            rutaAnterior = documento.getRutaArchivo();
            documento.setRutaArchivo(archivoService.subirDocumento(archivo, h.carpeta()));
        }
        documento.setNombre(request.nombre().trim());
        documento.setFechaEmision(request.fechaEmision());
        documento.setFechaVencimiento(request.fechaVencimiento());
        documento.actualizarEstado();

        Documento actualizado = h.guardar(documento);
        h.actualizarHabilitacion(idEntidad);
        if (rutaAnterior != null) archivoService.eliminarArchivo(rutaAnterior);  // al final
        return entityToResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long idEntidad, Long idDocumento, RolDocumento rol) {
        IDocumentoHandler h = handler(rol);
        Documento documento = h.obtener(idEntidad, idDocumento);
        String ruta = documento.getRutaArchivo();

        h.eliminar(documento);
        h.actualizarHabilitacion(idEntidad);
        archivoService.eliminarArchivo(ruta);                                     // al final
    }

    @Override
    @Transactional
    public void actualizarEstadosDocumentos(Long idEntidad, RolDocumento rol) {
        IDocumentoHandler h = handler(rol);

        // 1. Recalcula el estado de cada documento (VIGENTE / POR VENCER / VENCIDO)
        h.listar(idEntidad).forEach(Documento::actualizarEstado);

        // 2. Recalcula si el camión o conductor sigue habilitado
        h.actualizarHabilitacion(idEntidad);
    }

    @Override
    public ResultadoActualizacion actualizarTodosLosEstados() {
        int procesados = 0;
        int errores = 0;

        for (IDocumentoHandler h : handlers.values()) {
            for (Long id : h.listarIdsEntidades()) {
                try {
                    transactionTemplate.executeWithoutResult(status -> actualizarEstadosEntidad(h, id));
                    procesados++;
                } catch (Exception e) {
                    errores++;
                    log.error("No se pudo actualizar {} con ID {}", h.rol(), id, e);
                }
            }
        }
        log.info("Actualización de estados: {} procesados, {} con error", procesados, errores);
        return new ResultadoActualizacion(procesados, errores);
    }

    // Lógica compartida con el metodo por ID
    private void actualizarEstadosEntidad(IDocumentoHandler h, Long idEntidad) {
        h.listar(idEntidad).forEach(Documento::actualizarEstado);
        h.actualizarHabilitacion(idEntidad);
    }

}
