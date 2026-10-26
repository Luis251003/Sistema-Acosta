package com.upc.acosta.app.service;

import org.springframework.stereotype.Service;

@Service
public class UbigeoService {
//    private static final Pattern CODIGO_DEPARTAMENTO = Pattern.compile("\\d{2}");
//    private static final Pattern CODIGO_PROVINCIA = Pattern.compile("\\d{4}");
//
//    private final IUbigeoRepository ubigeoRepository;
//
//    public UbigeoService(IUbigeoRepository ubigeoRepository) {
//        this.ubigeoRepository = ubigeoRepository;
//    }
//
//    @Override
//    @Cacheable("ubigeo-departamentos")
//    public List<UbigeoDTOResponse> listarDepartamentos() {
//        return ordenarPorNombre(ubigeoRepository.findDepartamentos());
//    }
//
//    @Override
//    @Cacheable(cacheNames = "ubigeo-provincias", key = "#codigoDepartamento")
//    public List<UbigeoDTOResponse> listarProvincias(String codigoDepartamento) {
//        validarCodigo(codigoDepartamento, CODIGO_DEPARTAMENTO, "El código de departamento debe tener 2 dígitos numéricos");
//        return ordenarPorNombre(ubigeoRepository.findProvincias(codigoDepartamento));
//    }
//
//    @Override
//    @Cacheable(cacheNames = "ubigeo-distritos", key = "#codigoProvincia")
//    public List<UbigeoDTOResponse> listarDistritos(String codigoProvincia) {
//        validarCodigo(codigoProvincia, CODIGO_PROVINCIA, "El código de provincia debe tener 4 dígitos numéricos");
//        return ordenarPorNombre(ubigeoRepository.findDistritos(codigoProvincia));
//    }
//
//    private void validarCodigo(String codigo, Pattern formato, String mensaje) {
//        if (codigo == null || !formato.matcher(codigo).matches()) {
//            throw new BadRequestException(mensaje);
//        }
//    }
//
//    private List<UbigeoDTOResponse> ordenarPorNombre(List<UbigeoDTOResponse> opciones) {
//        return opciones.stream()
//                .sorted(Comparator.comparing(UbigeoDTOResponse::nombre, String.CASE_INSENSITIVE_ORDER)
//                        .thenComparing(UbigeoDTOResponse::codigo))
//                .toList();
//    }
}
