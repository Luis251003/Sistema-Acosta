package com.upc.acosta.app.service;

import com.upc.acosta.app.entity.Usuario;
import com.upc.acosta.app.repository.IUsuarioRepository;
import com.upc.acosta.app.security.UsuarioPrincipal;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        //Obtenemos el usuario X correo
        Usuario user = repository.findByEmail(username).orElseThrow(()-> new RuntimeException("Error al iniciar sesión"));
        //Obtenemos los roles del usuario
        List<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());
        return new UsuarioPrincipal(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }
}
