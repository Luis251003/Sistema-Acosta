package com.upc.acosta.app.service;

import com.upc.acosta.app.entity.Usuario;
import com.upc.acosta.app.error.EmailExistException;
import com.upc.acosta.app.repository.IUsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceJPA implements UserDetailsService {

    private final IUsuarioRepository repository;

    public UserDetailsServiceJPA(IUsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Obtenemos el usuario X correo
        Usuario user = repository.findByEmail(username).orElseThrow(()-> new RuntimeException("Error al iniciar sesión"));
        //Obtenemos los roles del usuario
        List<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList());
        return new User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
