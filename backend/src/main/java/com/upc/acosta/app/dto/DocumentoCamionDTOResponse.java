package com.upc.acosta.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentoCamionDTOResponse(
        Long id,
        Long idCamion,
        String nombre,
        String tipoDocumento,
        String rutaArchivo,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        String estado,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
