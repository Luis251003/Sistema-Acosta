package com.upc.acosta.app.data;

import com.upc.acosta.app.entity.Empleado;
import com.upc.acosta.app.entity.Rol;
import com.upc.acosta.app.entity.Usuario;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.IEmpleadoRepository;
import com.upc.acosta.app.repository.IRolRepository;
import com.upc.acosta.app.repository.IUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private final IEmpleadoRepository empleadoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(IEmpleadoRepository empleadoRepository,IUsuarioRepository usuarioRepository, IRolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
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
                    new Empleado("Luis","Cabrera","999999999","88888888"),
                    "luisortiz251003@gmail.com",
                    "Admin123¡",
                    rolAdmin
            );
            crearUsuario(
                    new Empleado("Bryan","Guerra","999999998","88888887"),
                    "bryanguerraperales412@gmail.com",
                    "Admin123¡",
                    rolAdmin
            );
            crearUsuario(
                    new Empleado("Conductor","Uno","999999997","88888886"),
                    "usuario.conductor@empresa.com",
                    "Password123",
                    rolConductor
            );
            crearUsuario(
                    new Empleado("Operador","Uno","999999996","88888885"),
                    "usuario.operador@empresa.com",
                    "Password123",
                    rolOperador
            );
        };
    }

    private void crearUsuario(
            Empleado empleado,
            String email,
            String password,
            Rol rol
    ) {
        //Validamos que el empleado no este registrado
        if(!empleadoRepository.existsByDni(empleado.getDni())){
            Empleado bean = empleadoRepository.save(empleado);
            //Validamos que el correo no este registrado
            if (usuarioRepository.findByEmail(email).isEmpty()) {

                //Creamos el objeto usuario
                Usuario usuario = new Usuario(email, passwordEncoder.encode(password), bean);

                //Configuramos los datos del usuario
                usuario.agregarRol(rol);

                //Regisramos
                usuarioRepository.save(usuario);
            }
        }
    }

}
