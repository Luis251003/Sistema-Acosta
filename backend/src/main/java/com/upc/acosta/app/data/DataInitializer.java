package com.upc.acosta.app.data;

import com.upc.acosta.app.entity.Rol;
import com.upc.acosta.app.entity.Usuario;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.IRolRepository;
import com.upc.acosta.app.repository.IUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(IUsuarioRepository usuarioRepository, IRolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initData(
    ){
        return args -> {
            // Buscar roles
            Rol rolAdmin = rolRepository
                    .findByNombre("ADMIN")
                    .orElseThrow(() ->
                            new NotFoundException("El rol ADMIN no está registrado")
                    );

            Rol rolOperador = rolRepository
                    .findByNombre("OPERADOR")
                    .orElseThrow(() ->
                            new NotFoundException("El rol OPERADOR no está registrado")
                    );
            Rol rolConductor = rolRepository
                    .findByNombre("CONDUCTOR")
                    .orElseThrow(() ->
                            new NotFoundException("El rol CONDUCTOR no está registrado")
                    );
            // Crear usuarios iniciales
            crearUsuario(
                    "luisortiz251003@gmail.com",
                    "Admin123¡",
                    rolAdmin,
                    true
            );
            crearUsuario(
                    "bryanguerraperales412@gmail.com",
                    "Admin123¡",
                    rolAdmin,
                    true
            );
            crearUsuario(
                    "usuario.bloqueado@empresa.com",
                    "Password123",
                    rolAdmin,
                    false
            );
        };
    }

    private void crearUsuario(
            String email,
            String password,
            Rol rol,
            boolean activo
    ) {
        //Validamos que el correo no este registrado
        if (usuarioRepository.findByEmail(email).isEmpty()) {

            //Creamos el objeto usuario
            Usuario usuario = new Usuario();

            //Configuramos los datos del usuario
            usuario.setEmail(email);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.agregarRol(rol);
            usuario.setActivo(activo);

            //Regisramos
            usuarioRepository.save(usuario);
        }
    }

}
