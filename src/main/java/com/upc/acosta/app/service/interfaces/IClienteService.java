package com.upc.acosta.app.service.interfaces;

import com.upc.acosta.app.dto.ClienteDTORequest;
import com.upc.acosta.app.dto.ClienteDTOResponse;
import com.upc.acosta.app.dto.ClienteUpdateDTORequest;
import com.upc.acosta.app.entity.Cliente;

import java.util.List;

public interface IClienteService {

    //METODOS DE CONVERSION
    ClienteDTOResponse entityToResponse(Cliente bean);

    List<ClienteDTOResponse> listar();
    ClienteDTOResponse buscarXId(Long id);
    ClienteDTOResponse buscarXRuc(String ruc);
    ClienteDTOResponse registrar(ClienteDTORequest cliente);
    ClienteDTOResponse editar(long id, ClienteUpdateDTORequest cliente);
    void eliminar(long id);
    List<ClienteDTOResponse> buscarXRazonSocialORuc(String texto);
}
