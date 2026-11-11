package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.*;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.entity.tipos.TipoVehiculo;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.ICamionRepository;
import com.upc.acosta.app.repository.IFlotaRepository;
import com.upc.acosta.app.repository.IOperacionRepository;
import com.upc.acosta.app.repository.ITipoDocumentoRepository;
import com.upc.acosta.app.service.interfaces.ICamionService;
import com.upc.acosta.app.service.interfaces.IDocumentoService;
import com.upc.acosta.app.specification.CamionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CamionService implements ICamionService {

    private final ICamionRepository camionRepository;
    private final ITipoDocumentoRepository iTipoDocumentoRepository;
    private final IDocumentoService documentoService;
    private final IOperacionRepository operacionRepository;
    private final IFlotaRepository flotaRepository;

    private Camion requestToEntity(CamionDTORequest request) {
        return new Camion(
                request.placa().trim().toUpperCase(Locale.ROOT),
                request.marca().trim(),
                request.modelo().trim(),
                new BigDecimal(request.capacidad().trim()),
                request.numeroTuce().trim(),
                request.tipoVehiculo()
        );
    }

    private CamionSeleccionadoDTOResponse camionSeleccionadoToResponse(CamionDTOResponse camion,boolean disponibilidad){
        return new CamionSeleccionadoDTOResponse(
                camion.idCamion(),
                camion.placa(),
                camion.marca(),
                camion.modelo(),
                camion.capacidad(),
                camion.numeroTuce(),
                camion.tipoVehiculo(),
                camion.habilitado(),
                disponibilidad
        );
    }

    private CamionDTOResponse entityToResponse(Camion camion) {
        return new CamionDTOResponse(
                camion.getId(),
                camion.getPlaca(),
                camion.getMarca(),
                camion.getModelo(),
                camion.getCapacidad().toPlainString(),
                camion.getNumeroTuce(),
                camion.getTipoVehiculo(),
                camion.isHabilitado()
        );
    }

    private TipoDocumentoDTOResponse entityTipoDocumentoToResponse(TipoDocumento bean){
        return new TipoDocumentoDTOResponse(
                bean.getIdTipoDocumento(),
                bean.getNombre(),
                bean.getRol().toString()
        );
    }

    @Override
    @Transactional
    public CamionDTOResponse registrar(CamionDTORequest request) {
        String placa = request.placa().trim();
        if (camionRepository.existsByPlacaIgnoreCase(placa)) {
            throw new DataExistException("La placa " + placa + " ya está registrada");
        }

        String numeroTuce = request.numeroTuce().trim();
        if (camionRepository.existsByNumeroTuceIgnoreCase(numeroTuce)) {
            throw new DataExistException("El número TUCE " + numeroTuce + " ya está registrado");
        }

        return entityToResponse(camionRepository.save(requestToEntity(request)));
    }

    @Override
    @Transactional
    public CamionDTOResponse actualizar(Long id, CamionDTORequest request) {
        Camion camion = camionRepository.findById(id)
                .filter(existente -> Boolean.TRUE.equals(existente.getActivo()))
                .orElseThrow(() -> new NotFoundException("Camión con ID: " + id + " no encontrado"));

        String placa = request.placa().trim();
        if (camionRepository.existsByPlacaIgnoreCaseAndIdNot(placa, id)) {
            throw new DataExistException("La placa " + placa + " ya está registrada");
        }

        String numeroTuce = request.numeroTuce().trim();
        if (camionRepository.existsByNumeroTuceIgnoreCaseAndIdNot(numeroTuce, id)) {
            throw new DataExistException("El número TUCE " + numeroTuce + " ya está registrado");
        }

        Camion actualizado = requestToEntity(request);
        camion.setPlaca(actualizado.getPlaca());
        camion.setMarca(actualizado.getMarca());
        camion.setModelo(actualizado.getModelo());
        camion.setCapacidad(actualizado.getCapacidad());
        camion.setNumeroTuce(actualizado.getNumeroTuce());
        camion.setTipoVehiculo(actualizado.getTipoVehiculo());

        return entityToResponse(camionRepository.save(camion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CamionDTOResponse> listar(String busqueda, TipoVehiculo tipoVehiculo) {
        Specification<Camion> spec = CamionSpecification.filtrar(busqueda,tipoVehiculo);
        return camionRepository.findAll(spec).stream()
                .map(this::entityToResponse)
                .toList();
    }

    @Override
    public CamionSeleccionadoDTOResponse buscarXIdAndDisponibilidad(Long idCamion, Long idOperacion) {
        //Actualizamos el estado de habilitacion del camion
        documentoService.actualizarEstadosDocumentos(idCamion,RolDocumento.CAMION);
        //Obtenemos el camion actualizado
        Camion camion = camionRepository.findById(idCamion)
                .orElseThrow(() -> new NotFoundException("Camion con ID: " + idCamion + " no encontrado"));
        return buscarDisponibilidad(camion,idOperacion);
    }

    @Override
    public CamionSeleccionadoDTOResponse buscarXPlacaAndDisponibilidad(String placa, Long idOperacion) {
        Camion camion = camionRepository.findByPlaca(placa)
                .orElseThrow(() -> new NotFoundException("Camion con placa: " + placa + " no encontrado"));
        documentoService.actualizarEstadosDocumentos(camion.getId(), RolDocumento.CAMION);
        Camion camionSaved = camionRepository.findById(camion.getId())
                .orElseThrow(() -> new NotFoundException("Camion con ID: " + camion.getId() + " no encontrado"));
        return buscarDisponibilidad(camionSaved,idOperacion);
    }

    @Override
    public Set<TipoDocumentoDTOResponse> listarTiposDocumento() {
        return iTipoDocumentoRepository.findByRol(RolDocumento.CAMION).stream().map(this::entityTipoDocumentoToResponse).collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public CamionDTOResponse buscarXId(Long id) {
        return camionRepository.findById(id)
                .filter(camion -> Boolean.TRUE.equals(camion.getActivo()))
                .map(this::entityToResponse)
                .orElseThrow(() -> new NotFoundException("Camión con ID: " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CamionDTOResponse> buscarPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return List.of();
        }

        return camionRepository.findByPlacaContainingIgnoreCase(placa.trim())
                .stream()
                .filter(camion -> Boolean.TRUE.equals(camion.getActivo()))
                .map(this::entityToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Camion camion = camionRepository.findById(id)
                .filter(existente -> Boolean.TRUE.equals(existente.getActivo()))
                .orElseThrow(() -> new NotFoundException("Camión con ID: " + id + " no encontrado"));

        camion.setActivo(false);
        camionRepository.save(camion);
    }

    private CamionSeleccionadoDTOResponse buscarDisponibilidad(Camion camion, Long idOperacion){
        if (idOperacion == null) {
            throw new BadRequestException("El ID de la operación es obligatorio");
        }
        Operacion operacion = operacionRepository.findById(idOperacion)
                .orElseThrow(() -> new NotFoundException("Operación con ID: " + idOperacion + " no encontrada"));

        LocalDate fechaRecojo = operacion.getFechaRecojoProgramado();
        if (fechaRecojo == null) {
            throw new BadRequestException(
                    "La operación con ID: " + idOperacion + " no tiene una fecha de entrega programada"
            );
        }

        boolean disponible = !flotaRepository.existsAsignacionCamionEnFecha(
                camion.getId(),
                fechaRecojo,
                operacion.getId()
        );
        CamionDTOResponse datos = entityToResponse(camion);
        return camionSeleccionadoToResponse(datos,disponible);

    }
}
