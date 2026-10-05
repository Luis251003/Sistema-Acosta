package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.UsuarioDTOResponse;

public interface IUsuarioService {
    UsuarioDTOResponse buscarXCorreo(String email);
}
