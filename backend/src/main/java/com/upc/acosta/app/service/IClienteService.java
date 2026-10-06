package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.ClienteDTORequest;
import com.upc.acosta.app.dto.ClienteDTOResponse;
import com.upc.acosta.app.dto.ClienteUpdateDTORequest;

import java.util.List;

public interface IClienteService {

    List<ClienteDTOResponse> listar();
    ClienteDTOResponse registrar(ClienteDTORequest cliente);
    ClienteDTOResponse editar(long id, ClienteUpdateDTORequest cliente);
    void eliminar(long id);
    List<ClienteDTOResponse> buscarXRazonSocialORuc(String texto);
}
