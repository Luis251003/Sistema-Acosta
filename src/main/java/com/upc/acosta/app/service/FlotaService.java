package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.FlotaDTORequest;
import com.upc.acosta.app.dto.FlotaDetalleDTORequest;
import com.upc.acosta.app.dto.FlotaDetalleDTOResponse;
import com.upc.acosta.app.dto.FlotaDTOResponse;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.entity.tipos.RolFlota;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.ICamionRepository;
import com.upc.acosta.app.repository.IConductorRepository;
import com.upc.acosta.app.repository.IFlotaRepository;
import com.upc.acosta.app.repository.IOperacionRepository;
import com.upc.acosta.app.service.interfaces.IFlotaService;
import com.upc.acosta.app.service.interfaces.IDocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlotaService implements IFlotaService {

    private final IFlotaRepository flotaRepo;
    private final IOperacionRepository operacionRepo;
    private final IConductorRepository conductorRepo;
    private final ICamionRepository camionRepo;
    private final IDocumentoService documentoService;

    private FlotaDTOResponse entityToResponse(Flota flota){
        List<FlotaDetalleDTOResponse> detalles = flota.getDetalles().stream()
                .map(detalle -> {
                    Empleado empleado = detalle.getConductor().getEmpleado();
                    String nombreConductor = empleado.getNombre() + " " + empleado.getApellido();
                    return new FlotaDetalleDTOResponse(
                            detalle.getId(),
                            nombreConductor,
                            detalle.getCamion().getPlaca(),
                            detalle.getRol()
                    );
                })
                .toList();

        return new FlotaDTOResponse(flota.getId(), detalles);
    }

    @Override
    @Transactional
    public FlotaDTOResponse registrar(FlotaDTORequest flota) {
        validarSolicitud(flota);

        //Buscamos a la operacion por ID
        Operacion operacion = operacionRepo.findById(flota.idOperacion())
                .orElseThrow(() -> new NotFoundException(
                        "Operación con ID: " + flota.idOperacion() + " no encontrada"
                ));

        //Validamos que la operacion no tenga una flota registrada
        if (flotaRepo.existsByOperacion_Id(flota.idOperacion())) {
            throw new DataExistException(
                    "La operación con ID: " + flota.idOperacion() + " ya tiene una flota registrada"
            );
        }

        validarDisponibilidad(operacion, flota);

        //Construimos una nueva flota
        Flota nuevaFlota = new Flota(operacion);
        //Agregamos los detalle de la flota
        agregarDetalle(nuevaFlota, flota.principal());
        if (flota.secundario() != null) {
            agregarDetalle(nuevaFlota, flota.secundario());
        }

        //Registramos la flota
        Flota flotaRegistrada = flotaRepo.save(nuevaFlota);
        return entityToResponse(flotaRegistrada);
    }

    @Override
    @Transactional
    public FlotaDTOResponse actualizar(FlotaDTORequest flota) {
        validarSolicitud(flota);

        Flota flotaExistente = flotaRepo.findByOperacion_Id(flota.idOperacion())
                .orElseThrow(() -> new NotFoundException(
                        "No se encontró una flota para la operación con ID: " + flota.idOperacion()
                ));

        validarDisponibilidad(flotaExistente.getOperacion(), flota);

        flotaExistente.getDetalles().clear();
        flotaRepo.flush();
        agregarDetalle(flotaExistente, flota.principal());
        if (flota.secundario() != null) {
            agregarDetalle(flotaExistente, flota.secundario());
        }

        Flota flotaActualizada = flotaRepo.saveAndFlush(flotaExistente);
        return entityToResponse(flotaActualizada);
    }

    private void validarDisponibilidad(Operacion operacion, FlotaDTORequest solicitud) {
        if (operacion.getFechaRecojoProgramado() == null) {
            throw new BadRequestException(
                    "La operación con ID: " + operacion.getId() + " no tiene una fecha de recojo programada"
            );
        }

        validarDetalleDisponible(operacion, solicitud.principal());
        if (solicitud.secundario() != null) {
            validarDetalleDisponible(operacion, solicitud.secundario());
        }
    }

    private void validarDetalleDisponible(Operacion operacion, FlotaDetalleDTORequest detalle) {
        Conductor conductor = conductorRepo.findById(detalle.idConductor())
                .orElseThrow(() -> new NotFoundException(
                        "Conductor con ID: " + detalle.idConductor() + " no encontrado"
                ));
        documentoService.actualizarEstadosDocumentos(conductor.getId(), RolDocumento.CONDUCTOR);
        conductor = conductorRepo.findById(conductor.getId())
                .orElseThrow(() -> new NotFoundException(
                        "Conductor con ID: " + detalle.idConductor() + " no encontrado"
                ));

        if (!Boolean.TRUE.equals(conductor.getActivo()) || !Boolean.TRUE.equals(conductor.getHabilitado())) {
            throw new BadRequestException(
                    "El conductor con ID: " + conductor.getId() + " no está habilitado"
            );
        }
        if (flotaRepo.existsAsignacionConductorEnFecha(
                conductor.getId(),
                operacion.getFechaRecojoProgramado(),
                operacion.getId()
        )) {
            throw new BadRequestException(
                    "El conductor con ID: " + conductor.getId() + " no está disponible para la fecha de recojo"
            );
        }

        Camion camion = camionRepo.findById(detalle.idCamion())
                .orElseThrow(() -> new NotFoundException(
                        "Camión con ID: " + detalle.idCamion() + " no encontrado"
                ));
        documentoService.actualizarEstadosDocumentos(camion.getId(), RolDocumento.CAMION);
        camion = camionRepo.findById(camion.getId())
                .orElseThrow(() -> new NotFoundException(
                        "Camión con ID: " + detalle.idCamion() + " no encontrado"
                ));

        if (!Boolean.TRUE.equals(camion.getActivo()) || !camion.isHabilitado()) {
            throw new BadRequestException(
                    "El camión con ID: " + camion.getId() + " no está habilitado"
            );
        }
        if (flotaRepo.existsAsignacionCamionEnFecha(
                camion.getId(),
                operacion.getFechaRecojoProgramado(),
                operacion.getId()
        )) {
            throw new BadRequestException(
                    "El camión con ID: " + camion.getId() + " no está disponible para la fecha de recojo"
            );
        }
    }

    private void agregarDetalle(Flota flota, FlotaDetalleDTORequest detalle) {
        Conductor conductor = conductorRepo.findById(detalle.idConductor())
                .orElseThrow(() -> new NotFoundException(
                        "Conductor con ID: " + detalle.idConductor() + " no encontrado"
                ));
        Camion camion = camionRepo.findById(detalle.idCamion())
                .orElseThrow(() -> new NotFoundException(
                        "Camión con ID: " + detalle.idCamion() + " no encontrado"
                ));
        flota.agregarDetalle(conductor, camion, detalle.rol());
    }

    private void validarRol(FlotaDetalleDTORequest detalle, RolFlota esperado, String nombreDetalle) {
        if (detalle == null) {
            throw new BadRequestException("El detalle " + nombreDetalle + " es obligatorio");
        }
        if (detalle.idConductor() == null || detalle.idCamion() == null) {
            throw new BadRequestException(
                    "El conductor y el camión son obligatorios en el detalle " + nombreDetalle
            );
        }
        if (detalle.rol() != esperado) {
            throw new BadRequestException(
                    "El rol del detalle " + nombreDetalle + " debe ser " + esperado
            );
        }
    }

    private void validarSolicitud(FlotaDTORequest flota) {
        if (flota == null || flota.idOperacion() == null) {
            throw new BadRequestException("El ID de la operación es obligatorio");
        }

        validarRol(flota.principal(), RolFlota.PRINCIPAL, "principal");
        if (flota.secundario() != null) {
            validarRol(flota.secundario(), RolFlota.SECUNDARIO, "secundario");
            if (flota.principal().idConductor().equals(flota.secundario().idConductor())) {
                throw new BadRequestException("El conductor no puede repetirse en los detalles de la flota");
            }
            if (flota.principal().idCamion().equals(flota.secundario().idCamion())) {
                throw new BadRequestException("El camión no puede repetirse en los detalles de la flota");
            }
        }
    }
}
