package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.RucDTOResponse;
import com.upc.acosta.app.entity.Cliente;
import com.upc.acosta.app.entity.Operacion;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.repository.IClienteRepository;
import com.upc.acosta.app.repository.IOperacionRepository;
import com.upc.acosta.app.validator.RucValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Locale;

@Service
public class OperacionService implements IOperacionService {
    private final IClienteRepository clienteRepository;
    private final IOperacionRepository operacionRepository;
    private final ConsultaRUCService consultaRUCService;

    public OperacionService(
            IClienteRepository clienteRepository,
            IOperacionRepository operacionRepository,
            ConsultaRUCService consultaRUCService
    ) {
        this.clienteRepository = clienteRepository;
        this.operacionRepository = operacionRepository;
        this.consultaRUCService = consultaRUCService;
    }

    @Override
    @Transactional(readOnly = true)
    public RucDTOResponse buscarClientePorRuc(String ruc) {
        if (!RucValidator.validar(ruc)) {
            throw new BadRequestException("El RUC ingresado no es válido");
        }

        return clienteRepository.findByRuc(ruc)
                .map(this::convertirARespuestaRuc)
                .orElseGet(() -> consultaRUCService.consultarRUC(ruc));
    }

    @Override
    public String generarCodigoServicio(Long numeroOperacion) {
        if (numeroOperacion == null || numeroOperacion < 1) {
            throw new BadRequestException("El número de operación debe ser positivo");
        }
        return "SRV-" + Year.now().getValue() + "-"
                + String.format(Locale.ROOT, "%05d", numeroOperacion);
    }

    @Override
    @Transactional
    public Operacion registrar(Operacion operacion) {
        operacion.getFlota().forEach(asignacion -> asignacion.setOperacion(operacion));
        operacion.setCodigo(null);
        Operacion registrada = operacionRepository.saveAndFlush(operacion);
        registrada.setCodigo(generarCodigoServicio(registrada.getId()));
        return operacionRepository.save(registrada);
    }

    private RucDTOResponse convertirARespuestaRuc(Cliente cliente) {
        return new RucDTOResponse(
                cliente.getRuc(),
                cliente.getRazonSocial(),
                cliente.getEstado().getNombre(),
                cliente.getCondicion().getNombre(),
                cliente.getDireccionFiscal()
        );
    }
}
