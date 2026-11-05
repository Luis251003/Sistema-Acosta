package com.upc.acosta.app.dto;

import com.upc.acosta.app.entity.tipos.TipoVehiculo;

public record CamionDTOResponse(
        Long idCamion,
        String placa,
        String marca,
        String modelo,
        String capacidad,
        String numeroTuce,
        TipoVehiculo tipoVehiculo,
        Boolean estado
) {
}
