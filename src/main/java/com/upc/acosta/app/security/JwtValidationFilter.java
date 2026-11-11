package com.upc.acosta.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;

import static com.upc.acosta.app.security.TokenJwtConfig.*;
import java.io.IOException;
import java.util.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    private final long jwtExpiration;
    private final SecretKey secretKey;

    public JwtValidationFilter(AuthenticationManager authenticationManager, long jwtExpiration, SecretKey secretKey) {
        super(authenticationManager);
        this.jwtExpiration = jwtExpiration;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        if(header == null || !header.startsWith(PREFIX_TOKEN)){
            chain.doFilter(request,response);
            return;
        }
        String token = header.replace(PREFIX_TOKEN,"");

        try{
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            String email = claims.getSubject();
            Long id = claims.get("id",Long.class);
            List<String> roles = claims.get("authorities", List.class);

            Collection<? extends GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsuarioPrincipal principal = new UsuarioPrincipal(id, email, null, authorities);
            UsernamePasswordAuthenticationToken auth= new UsernamePasswordAuthenticationToken(principal,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            //4. Renovar TOKEN si esta proximo a expirar
            Date expiration = claims.getExpiration();
            long tiempoRestante = expiration.getTime() - System.currentTimeMillis();
            long tiempoRenovacion = 15 * 60 * 1000L;

            if(tiempoRestante <= tiempoRenovacion){
                String nuevoToken = generarToken(email,authorities);
                response.setHeader(HEADER_AUTHORIZATION,PREFIX_TOKEN + nuevoToken);
            }
            chain.doFilter(request,response);
        }catch (JwtException e){
            Map<String,String> body = new HashMap<>();
            body.put("error",e.getMessage());
            body.put("message","El token JWT es invalido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(CONTENT_TYPE);
        }
    }
    private String generarToken(String email,Collection<? extends GrantedAuthority> roles){
        //Generamos el token
        return Jwts
                .builder()
                .subject(email)
                .claim("authorities", roles.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();
    }
}
