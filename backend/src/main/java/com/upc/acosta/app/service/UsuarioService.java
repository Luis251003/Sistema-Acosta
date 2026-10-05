package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.UsuarioDTOResponse;
import com.upc.acosta.app.entity.Usuario;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements IUsuarioService{

    //REPOSITORIO
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    //METODOS DE CONVERSION
    private UsuarioDTOResponse entityToResponse(Usuario user){
        return new UsuarioDTOResponse(user.getId(),user.getEmail(),user.getRoles());
    }

    //BUSCAR USUARIO POR CORREO
    @Override
    @Transactional(readOnly = true)
    public UsuarioDTOResponse buscarXCorreo(String email) {
        return usuarioRepository
                .findByEmail(email)
                .map(this::entityToResponse)
                .orElseThrow(()->new NotFoundException("El usuario con email "+ email +"no ha sido encontrado"));
    }
}
