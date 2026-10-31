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
        //Obtener empleado
        var empleado = user.getEmpleado();
        String firstName = empleado.getNombre().split("\\s+")[0];
        String firstLastname = empleado.getApellido().split("\\s+")[0];
        String nombre = firstName.concat(" " + firstLastname);
        //Devolvemos el usuario encontrado
        return new UsuarioDTOResponse(user.getId(),user.getEmail(),nombre);
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
