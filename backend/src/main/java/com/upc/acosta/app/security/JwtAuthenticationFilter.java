package com.upc.acosta.app.security;

import com.upc.acosta.app.entity.Usuario;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.upc.acosta.app.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final long jwtExpiration;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, long jwtExpiration) {
        this.authenticationManager = authenticationManager;
        this.jwtExpiration=jwtExpiration;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Usuario bean;
        String email = null;
        String password = null;
        try{
            //Obtenemos el usuario
            bean = new ObjectMapper().readValue(request.getInputStream(),Usuario.class);
            //Obtenemos el correo
            email = bean.getEmail();
            //Obtenemos el password
            password = bean.getPassword();
        }catch (StreamReadException | DatabindException | IOException e){
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        //Obtenemos al usuario
        User user = (User)authResult.getPrincipal();
        //Validamos que el usuario exista
        if(user==null)throw new RuntimeException("Error de autenticación");

        String email = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        //Generamos el token
        String token = Jwts
                .builder()
                .subject(email)
                .claim("authorities", roles.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();
        //Agregamos el token al encabezado
        response.addHeader(HEADER_AUTHORIZATION,PREFIX_TOKEN + token);
        //Configuramos la respuesta del cuerpo
        Map<String,String> body = new HashMap<>();
        body.put("token",token);
        body.put("email",user.getUsername());
        body.put("message","Inicio de sesión exitoso");
        //Devolvemos el body
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //Configuramos la respuesta del cuerpo
        Map<String,String> body = new HashMap<>();
        body.put("message","Username y/o password incorrectos");
        body.put("error",failed.getMessage());
        //Devolvemos el body
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);
    }
}
