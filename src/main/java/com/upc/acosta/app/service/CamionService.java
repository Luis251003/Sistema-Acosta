package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.CamionDTORequest;
import com.upc.acosta.app.dto.CamionDTOResponse;
import com.upc.acosta.app.dto.TipoDocumentoDTOResponse;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.ICamionRepository;
import com.upc.acosta.app.repository.ITipoDocumentoRepository;
import com.upc.acosta.app.specification.CamionSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CamionService implements ICamionService {

    private final ICamionRepository camionRepository;
    private final ITipoDocumentoRepository iTipoDocumentoRepository;

    public CamionService(ICamionRepository camionRepository, ITipoDocumentoRepository iTipoDocumentoRepository) {
        this.camionRepository = camionRepository;
        this.iTipoDocumentoRepository = iTipoDocumentoRepository;
    }

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

    private CamionDTOResponse entityToResponse(Camion camion) {
        return new CamionDTOResponse(
                camion.getId(),
                camion.getPlaca(),
                camion.getMarca(),
                camion.getModelo(),
                camion.getCapacidad().toPlainString(),
                camion.getNumeroTuce(),
                camion.getTipoVehiculo(),
                camion.getEstado() == EstadoCamion.HABILITADO
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
}
