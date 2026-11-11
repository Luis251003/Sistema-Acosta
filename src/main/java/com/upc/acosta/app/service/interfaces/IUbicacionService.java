package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.UbicacionDTORequest;
import com.upc.acosta.app.dto.UbicacionDTOResponse;
import com.upc.acosta.app.dto.UbigeoCompletoDTOResponse;
import com.upc.acosta.app.entity.Ubicacion;
import com.upc.acosta.app.entity.UbigeoDistrito;

public interface IUbicacionService {

    UbicacionDTOResponse entityUbicacionToResponse(Ubicacion ubicacion);
    UbigeoCompletoDTOResponse entityUbigeoToResponse(UbigeoDistrito ubigeoDistrito);
    Ubicacion buscarORegistrarUbicacion(UbicacionDTORequest ubicacion);
    Ubicacion registrarUbicacion(UbicacionDTORequest ubicacion);
}
