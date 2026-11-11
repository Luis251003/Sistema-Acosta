package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.UbigeoDTOResponse;
import com.upc.acosta.app.entity.UbigeoDepartamento;
import com.upc.acosta.app.repository.IUbigeoDepartamentoRepository;
import com.upc.acosta.app.repository.IUbigeoDistritoRepository;
import com.upc.acosta.app.repository.IUbigeoProvinciaRepository;
import com.upc.acosta.app.service.interfaces.IUbigeoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UbigeoService implements IUbigeoService {

    //Obtenemos el repositorio
    private final IUbigeoDepartamentoRepository ubigeoDepartamentoRepository;
    private final IUbigeoProvinciaRepository ubigeoProvinciaRepository;
    private final IUbigeoDistritoRepository ubigeoDistritoRepository;

    //Metodo constructor
    public UbigeoService(IUbigeoDepartamentoRepository ubigeoRepository, IUbigeoProvinciaRepository ubigeoProvinciaRepository, IUbigeoDistritoRepository ubigeoDistritoRepository) {
        this.ubigeoDepartamentoRepository = ubigeoRepository;
        this.ubigeoProvinciaRepository = ubigeoProvinciaRepository;
        this.ubigeoDistritoRepository = ubigeoDistritoRepository;
    }

    //Metodo para mapear los departamentos
    public UbigeoDTOResponse mapearDepartamento(UbigeoDepartamento departamento) {
        return new UbigeoDTOResponse(
                departamento.getId(),
                departamento.getDepartamento(),
                departamento.getUbigeo()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UbigeoDTOResponse> listarDepartamentos() {
        return ubigeoDepartamentoRepository.findAll().stream()
                .map(this::mapearDepartamento)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UbigeoDTOResponse> listarProvincias(Long idDepartamento) {
        return ubigeoProvinciaRepository.findByUbigeoDepartamentoId(idDepartamento).stream()
                .map(provincia -> new UbigeoDTOResponse(provincia.getId(), provincia.getProvincia(), provincia.getUbigeo()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UbigeoDTOResponse> listarDistritos(Long idProvincia) {
        return ubigeoDistritoRepository.findByUbigeoProvinciaId(idProvincia).stream()
                .map(distrito -> new UbigeoDTOResponse(distrito.getId(), distrito.getDistrito(),distrito.getUbigeo()))
                .collect(Collectors.toList());
    }
}
