package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.UbicacionDTORequest;
import com.upc.acosta.app.dto.UbicacionDTOResponse;
import com.upc.acosta.app.dto.UbigeoCompletoDTOResponse;
import com.upc.acosta.app.entity.Ubicacion;
import com.upc.acosta.app.entity.UbigeoDistrito;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.IUbicacionRepository;
import com.upc.acosta.app.repository.IUbigeoDistritoRepository;
import com.upc.acosta.app.service.interfaces.IUbicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UbicacionService implements IUbicacionService {

    private final IUbicacionRepository ubicacionRepository;
    private final IUbigeoDistritoRepository ubigeoRepository;

    @Override
    public UbicacionDTOResponse entityUbicacionToResponse(Ubicacion ubicacion) {

        //Obtenemos el ubigeo dto
        UbigeoCompletoDTOResponse ubigeo = this.entityUbigeoToResponse(ubicacion.getUbigeo());

        return new UbicacionDTOResponse(
                ubicacion.getId(),
                ubigeo,
                ubicacion.getLatitud(),
                ubicacion.getLongitud(),
                ubicacion.getDireccion()
        );
    }

    @Override
    public UbigeoCompletoDTOResponse entityUbigeoToResponse(UbigeoDistrito ubigeoDistrito) {
        return new UbigeoCompletoDTOResponse(
                ubigeoDistrito.getUbigeo(),
                ubigeoDistrito.getDistrito(),
                ubigeoDistrito.getUbigeoProvincia().getProvincia(),
                ubigeoDistrito.getUbigeoProvincia().getUbigeoDepartamento().getDepartamento()
        );
    }

    @Override
    public Ubicacion buscarORegistrarUbicacion(UbicacionDTORequest ubicacion) {
        Optional<Ubicacion> ubicacionExistente = ubicacionRepository.findByDireccionAndUbigeoUbigeo(ubicacion.direccion(), ubicacion.ubigeo());
        return ubicacionExistente.orElseGet(() -> this.registrarUbicacion(ubicacion));
    }

    @Override
    public Ubicacion registrarUbicacion(UbicacionDTORequest ubicacion) {
        //Buscamos el ubigeo de ubicacion partida
        UbigeoDistrito ubigeo = ubigeoRepository.findByUbigeo(ubicacion.ubigeo()).orElseThrow(() -> new NotFoundException("Ubigeo con código: " + ubicacion.ubigeo() + " no encontrado"));
        Ubicacion ubicacionPartida = new Ubicacion(ubigeo, ubicacion.direccion(), ubicacion.latitud(), ubicacion.longitud());
        return ubicacionRepository.save(ubicacionPartida);
    }
}
