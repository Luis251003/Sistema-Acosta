package com.upc.acosta.app.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.SecretKey;
import java.util.Arrays;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Bean
    public SecretKey jwtSecretKey(@Value("${JWT_SECRET}") String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    AuthenticationManager authenticationManager(){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity,SecretKey secretKey){
        return httpSecurity
        .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(),jwtExpiration,secretKey))
        .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager(),jwtExpiration,secretKey))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors->cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests((auth)->auth
                // Solo ADMIN y OPERADOR
                .requestMatchers("/api/usuario/**")
                .hasAnyRole("ADMIN", "OPERADOR")

                .requestMatchers("/api/cliente/**")
                .hasAnyRole("ADMIN", "OPERADOR")

                .anyRequest()
                .authenticated()
        )
        .sessionManagement(management->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT"));
        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}
