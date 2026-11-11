package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.OperacionCompletaDTOResponse;
import com.upc.acosta.app.dto.OperacionDTORequest;
import com.upc.acosta.app.dto.OperacionDTOResponse;
import com.upc.acosta.app.dto.OperacionListarDTOResponse;
import com.upc.acosta.app.entity.tipos.EstadoOperacion;

import java.time.LocalDate;
import java.util.List;

public interface IOperacionService {
    OperacionDTOResponse registrar(OperacionDTORequest operacion, Long id);
    OperacionDTOResponse actualizar(OperacionDTORequest operacion, Long operacionId, Long usuarioId);
    List<OperacionListarDTOResponse> listar(String busqueda, LocalDate fechaInicio, LocalDate fechaFin, List<String> placas, List<EstadoOperacion> estados);
    OperacionCompletaDTOResponse buscarXId(Long id);
}
