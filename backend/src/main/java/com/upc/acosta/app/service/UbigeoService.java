package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.UbigeoDTOOpcion;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.repository.IUbigeoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UbigeoService implements IUbigeoService {
    private static final Pattern CODIGO_DEPARTAMENTO = Pattern.compile("\\d{2}");
    private static final Pattern CODIGO_PROVINCIA = Pattern.compile("\\d{4}");

    private final IUbigeoRepository ubigeoRepository;

    public UbigeoService(IUbigeoRepository ubigeoRepository) {
        this.ubigeoRepository = ubigeoRepository;
    }

    @Override
    @Cacheable("ubigeo-departamentos")
    public List<UbigeoDTOOpcion> listarDepartamentos() {
        return ordenarPorNombre(ubigeoRepository.findDepartamentos());
    }

    @Override
    @Cacheable(cacheNames = "ubigeo-provincias", key = "#codigoDepartamento")
    public List<UbigeoDTOOpcion> listarProvincias(String codigoDepartamento) {
        validarCodigo(codigoDepartamento, CODIGO_DEPARTAMENTO, "El código de departamento debe tener 2 dígitos numéricos");
        return ordenarPorNombre(ubigeoRepository.findProvincias(codigoDepartamento));
    }

    @Override
    @Cacheable(cacheNames = "ubigeo-distritos", key = "#codigoProvincia")
    public List<UbigeoDTOOpcion> listarDistritos(String codigoProvincia) {
        validarCodigo(codigoProvincia, CODIGO_PROVINCIA, "El código de provincia debe tener 4 dígitos numéricos");
        return ordenarPorNombre(ubigeoRepository.findDistritos(codigoProvincia));
    }

    private void validarCodigo(String codigo, Pattern formato, String mensaje) {
        if (codigo == null || !formato.matcher(codigo).matches()) {
            throw new BadRequestException(mensaje);
        }
    }

    private List<UbigeoDTOOpcion> ordenarPorNombre(List<UbigeoDTOOpcion> opciones) {
        return opciones.stream()
                .sorted(Comparator.comparing(UbigeoDTOOpcion::nombre, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(UbigeoDTOOpcion::codigo))
                .toList();
    }
}
