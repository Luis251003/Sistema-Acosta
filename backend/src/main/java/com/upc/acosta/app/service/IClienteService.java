package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.ClienteDTORequest;
import com.upc.acosta.app.dto.ClienteDTOResponse;

import java.util.List;

public interface IClienteService {

    List<ClienteDTOResponse> listar();
    ClienteDTOResponse buscarXId(Long id);
    ClienteDTOResponse registrar(ClienteDTORequest cliente);
    ClienteDTOResponse editar(long id,ClienteDTORequest cliente);
    void eliminar(long id);
    List<ClienteDTOResponse> buscarXRazonSocialORuc(String texto);
}
