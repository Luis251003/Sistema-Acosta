package com.upc.acosta.app.security;

import com.upc.acosta.app.entity.Usuario;
import com.upc.acosta.app.error.ValidAuthenticationException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

import static com.upc.acosta.app.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final long jwtExpiration;
    private final SecretKey secretKey;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, long jwtExpiration, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtExpiration=jwtExpiration;
        this.secretKey=secretKey;
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

            //Validamos que los campos no esten vacíos
            if (email == null || email.isBlank()) throw new ValidAuthenticationException("El email es obligatorio");

            if (password == null || password.isBlank()) throw new ValidAuthenticationException("La contraseña es obligatoria");

        }catch (StreamReadException | DatabindException | IOException e){
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        //Obtenemos al usuario
        UsuarioPrincipal user = (UsuarioPrincipal)authResult.getPrincipal();
        //Validamos que el usuario exista
        if(user==null)throw new RuntimeException("Error de autenticación");
        //Obtenemos el correo
        String email = user.getUsername();

        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        //Convertimos los roles a una lista de String
        List<String> roleNames = roles.stream().map(GrantedAuthority::getAuthority).filter(role -> role.startsWith("ROLE_")).toList();

        //Generamos el token
        String token = Jwts
                .builder()
                .subject(email)
                .claim("id",user.getId())
                .claim("authorities", roleNames)
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();
        //Agregamos el token al encabezado
        response.addHeader(HEADER_AUTHORIZATION,PREFIX_TOKEN + token);
        //Configuramos la respuesta del cuerpo
        Map<String,Object> body = new HashMap<>();
        body.put("token",token);
        body.put("email",user.getUsername());
        body.put("message","Inicio de sesión exitoso");
        body.put("roles",roleNames);
        //Devolvemos el body
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //Configuramos la respuesta del cuerpo
        Map<String,String> body = new HashMap<>();

        if (failed instanceof ValidAuthenticationException) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            body.put("error", "Error de validación");
            body.put("message", failed.getMessage());
            response.setStatus(400);
        }else if (failed instanceof DisabledException) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", "Cuenta inactiva");
            body.put("message", "La cuenta del usuario se encuentra inactiva");
            response.setStatus(403);

        } else {
            body.put("error", "Error de autenticación");
            body.put("message", "Usuario y/o contraseña incorrecta");
            response.setStatus(401);
        }
        //Devolvemos el body
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);

    }
}
