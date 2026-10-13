package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.CamionDTORequest;
import com.upc.acosta.app.dto.CamionDTOResponse;
import com.upc.acosta.app.entity.Camion;
import com.upc.acosta.app.entity.EstadoCamion;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.ICamionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.List;

@Service
public class CamionService implements ICamionService {

    private final ICamionRepository camionRepository;

    public CamionService(ICamionRepository camionRepository) {
        this.camionRepository = camionRepository;
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
    public List<CamionDTOResponse> listar() {
        return camionRepository.findAll().stream()
                .filter(camion -> Boolean.TRUE.equals(camion.getActivo()))
                .map(this::entityToResponse)
                .toList();
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
    public List<CamionDTOResponse> buscarXNombreOPlaca(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return List.of();
        }

        String criterio = texto.trim();
        return camionRepository
                .findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCaseOrPlacaContainingIgnoreCase(
                        criterio,
                        criterio,
                        criterio
                )
                .stream()
                .filter(camion -> Boolean.TRUE.equals(camion.getActivo()))
                .map(this::entityToResponse)
                .toList();
    }
}
