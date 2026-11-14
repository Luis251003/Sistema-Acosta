package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.*;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.entity.tipos.EstadoOperacion;
import com.upc.acosta.app.entity.tipos.EstadoReserva;
import com.upc.acosta.app.entity.tipos.MetodoResolucion;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.*;
import com.upc.acosta.app.service.interfaces.*;
import com.upc.acosta.app.specification.OperacionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperacionService implements IOperacionService {
    private final IClienteRepository clienteRepository;
    private final IOperacionRepository operacionRepository;
    private final ICodigoOperacionRepository codigoOperacionRepository;
    private final IPuntoOrigenRepository puntoOrigenRepository;

    private final ICodigoOperacionService codigoOperacionService;
    private final IClienteService clienteService;
    private final IUbicacionService ubicacionService;

    //METODO DE MAPEO
    private OperacionDTOResponse entityToResponse(Operacion bean){
        return new OperacionDTOResponse(
                bean.getId(),
                clienteService.entityToResponse(bean.getCliente()),
                ubicacionService.entityUbicacionToResponse(bean.getPartida()),
                ubicacionService.entityUbicacionToResponse(bean.getDestino()),
                bean.getPuntoOrigen() != null ? bean.getPuntoOrigen().getNombre() : "Desconocido",
                bean.getCodigoOperacion().getCodigo(),
                bean.getEstado().toString(),
                bean.getObservacion(),
                bean.getTokenSeguimiento(),
                bean.getFechaRecojoProgramado()
        );
    }

    private OperacionListarDTOResponse entityToListarResponse(Operacion bean){
        Flota flota = bean.getFlota();
        FlotaDetalle detallePrincipal = flota == null ? null : flota.getDetalles().getFirst();
        Empleado conductor = detallePrincipal == null || detallePrincipal.getConductor() == null
                ? null
                : detallePrincipal.getConductor().getEmpleado();

        return new OperacionListarDTOResponse(
                bean.getId(),
                bean.getCodigoOperacion().getCodigo(),
                clienteService.entityToResponse(bean.getCliente()).razonSocial(),
                bean.getFechaRecojoProgramado(),
                conductor == null ? null : conductor.getNombre() + " " + conductor.getApellido(),
                detallePrincipal == null || detallePrincipal.getCamion() == null
                        ? null
                        : detallePrincipal.getCamion().getPlaca(),
                bean.getEstado()
        );
    }

    private OperacionCompletaDTOResponse entityToDetalleResponse(Operacion bean) {
        Flota flota = bean.getFlota();
        FlotaDTOResponse flotaResponse = flota == null
                ? null
                : new FlotaDTOResponse(
                        flota.getId(),
                        flota.getDetalles().stream()
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
                                .toList()
                );

        return new OperacionCompletaDTOResponse(entityToResponse(bean), flotaResponse);
    }

    @Override
    @Transactional
    public OperacionDTOResponse registrar(OperacionDTORequest bean, Long usuarioId) {
        CodigoOperacion codigoOperacion = codigoOperacionRepository.findById(bean.idCodigo())
                .orElseThrow(() -> new NotFoundException("Código de operación con ID: " + bean.idCodigo() + " no encontrado"));
        if (!codigoOperacion.getUsuario().getId().equals(usuarioId)
                || codigoOperacion.getEstado() != EstadoReserva.PENDIENTE) {
            throw new BadRequestException("El código de operación no está reservado para este usuario");
        }
        codigoOperacionService.confirmarUso(usuarioId, bean.idCodigo());

        Cliente cliente = clienteRepository
                .findById(bean.idCliente())
                .orElseThrow(()->new NotFoundException("Cliente con ID: " + bean.idCliente() + " no encontrado"));

        Ubicacion ubicacionPartidaSaved = ubicacionService.buscarORegistrarUbicacion(bean.ubicacionPartida());
        Ubicacion ubicacionDestinoSaved = ubicacionService.buscarORegistrarUbicacion(bean.ubicacionDestino());

        ResultadoOrigen resultadoOrigen = resolverResultadoOrigen(bean.resultadoOrigen());
        Operacion operacion = new Operacion(
                cliente,
                ubicacionPartidaSaved,
                ubicacionDestinoSaved,
                codigoOperacion,
                bean.fechaRecojo(),
                java.util.UUID.randomUUID().toString(),
                resultadoOrigen.metodo()
        );
        Operacion registrada = operacionRepository.saveAndFlush(operacion);
        return entityToResponse(registrada);
    }

    @Override
    @Transactional
    public OperacionDTOResponse actualizar(OperacionDTORequest bean, Long operacionId, Long usuarioId) {
        Operacion operacion = operacionRepository
                .findByIdAndCodigoOperacion_Usuario_Id(operacionId, usuarioId)
                .orElseThrow(() -> new NotFoundException("Operación con ID: " + operacionId + " no encontrada"));

        if (!operacion.getCodigoOperacion().getId().equals(bean.idCodigo())) {
            throw new BadRequestException("No se puede cambiar el código de una operación registrada");
        }

        Cliente cliente = clienteRepository
                .findById(bean.idCliente())
                .orElseThrow(() -> new NotFoundException("Cliente con ID: " + bean.idCliente() + " no encontrado"));
        Ubicacion ubicacionPartida = ubicacionService.buscarORegistrarUbicacion(bean.ubicacionPartida());
        Ubicacion ubicacionDestino = ubicacionService.buscarORegistrarUbicacion(bean.ubicacionDestino());
        ResultadoOrigen resultadoOrigen = resolverResultadoOrigen(bean.resultadoOrigen());

        aplicarDatosEditables(operacion, bean, cliente, ubicacionPartida, ubicacionDestino, resultadoOrigen);
        return entityToResponse(operacionRepository.saveAndFlush(operacion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OperacionListarDTOResponse> listar(String busqueda, LocalDate fechaInicio, LocalDate fechaFin, List<String> placas, List<EstadoOperacion> estados) {
        Specification<Operacion> spec = OperacionSpecification.filtrar(busqueda,fechaInicio,fechaFin,placas,estados);
        return operacionRepository.findAll(spec).stream()
                .map(this::entityToListarResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OperacionCompletaDTOResponse buscarXId(Long id) {
        Operacion operacion = operacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Operación con ID: " + id + " no encontrada"));
        return entityToDetalleResponse(operacion);
    }

    private ResultadoOrigen resolverResultadoOrigen(ResultadoOrigen resultado) {
        if (resultado.metodo() == null) {
            throw new BadRequestException("El método de resolución del punto de origen es obligatorio");
        }

        if (resultado.metodo() == MetodoResolucion.NO_ENCONTRADO) {
            if (resultado.punto() != null) {
                throw new BadRequestException("Un punto de origen no encontrado no puede incluir un punto");
            }
            return ResultadoOrigen.noEncontrado();
        }

        if (resultado.punto() == null || resultado.punto().getId() == null) {
            throw new BadRequestException("El punto de origen seleccionado es obligatorio");
        }
        PuntoOrigen puntoOrigen = puntoOrigenRepository.findById(resultado.punto().getId())
                .orElseThrow(() -> new NotFoundException(
                        "Punto de origen con ID: " + resultado.punto().getId() + " no encontrado"
                ));
        return new ResultadoOrigen(puntoOrigen, resultado.metodo());
    }

    private void aplicarDatosEditables(
            Operacion operacion,
            OperacionDTORequest bean,
            Cliente cliente,
            Ubicacion partida,
            Ubicacion destino,
            ResultadoOrigen resultadoOrigen
    ) {
        operacion.setCliente(cliente);
        operacion.setPartida(partida);
        operacion.setDestino(destino);
        operacion.setFechaRecojoProgramado(bean.fechaRecojo());
        operacion.setObservacion(bean.observacion() == null || bean.observacion().isBlank()
                ? null
                : bean.observacion().trim());
        operacion.setPuntoOrigen(resultadoOrigen.punto());
        operacion.setMetodoResolucion(resultadoOrigen.metodo());
    }
}
