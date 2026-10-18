package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.ConductorDTORequest;
import com.upc.acosta.app.dto.ConductorDTOResponse;
import com.upc.acosta.app.entity.Conductor;
import com.upc.acosta.app.entity.Persona;
import com.upc.acosta.app.entity.TipoConductor;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.IConductorRepository;
import com.upc.acosta.app.repository.IPersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ConductorService implements IConductorService{

    //REPOSITORIOS
    private IConductorRepository conductorRepository;
    private IPersonaRepository personaRepository;

    //SERVICIOS
    private IArchivoService archivoService;

    //METODO CONSTRUCTOR
    public ConductorService(
            IConductorRepository conductorRepository,
            IPersonaRepository personaRepository,
            IArchivoService archivoService
    ) {
        this.conductorRepository = conductorRepository;
        this.personaRepository = personaRepository;
        this.archivoService = archivoService;
    }

    //METODO DE MAPEO
    private ConductorDTOResponse entityToResponse(Conductor bean){
        //ID DE PERSONA
        long id = bean.getPersona().getId();
        //OBTENEMOS A LA PERSONA
        Persona persona = personaRepository.findById(bean.getPersona().getId()).orElseThrow(()->new NotFoundException("Persona con ID: " + id + "no encontrado"));
        return new ConductorDTOResponse(
                bean.getId(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getTelefono(),
                persona.getDni(),
                bean.getBrevete(),
                bean.getFotoUrl(),
                bean.getTipoConductor(),
                bean.getEstado()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConductorDTOResponse> listar() {
        return conductorRepository.findAll()
                .stream()
                .filter((x)->x.getActivo()==true)
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoConductor> listarTipos() {
        return List.of(TipoConductor.values());
    }

    @Override
    @Transactional(readOnly = true)
    public ConductorDTOResponse buscarXId(Long id) {
        return conductorRepository.findById(id)
                .map(this::entityToResponse)
                .orElseThrow(()->new NotFoundException("Conductor con ID: " + id + "no encontrado"));
    }

    @Override
    @Transactional
    public ConductorDTOResponse registrar(ConductorDTORequest bean) {
        //VALIDAR DUPLICADO DNI
        if(personaRepository.existsByDni(bean.dni())) throw new DataExistException("El DNI: " + bean.dni() + " ya está registrado");
        //VALIDAR DUPLICADO TELEFONO
        if(personaRepository.existsByTelefono(bean.telefono())) throw new DataExistException("El teléfono: " + bean.telefono() + " ya está registrado");
        //VALIDAR DUPLICADO BREVETE
        if(conductorRepository.existsByBrevete(bean.brevete())) throw new DataExistException("El brevete: " + bean.brevete() + " ya está registrado");

        //REGISTRAR PERSONA
        Persona persona = personaRepository.save(new Persona(bean.nombre(),bean.apellido(),bean.telefono(),bean.dni()));
        //CREAR CONDUCTOR
        Conductor conductor = new Conductor(persona, bean.brevete(), bean.tipoConductor());

        return entityToResponse(conductorRepository.save(conductor));
    }

    @Override
    @Transactional
    public ConductorDTOResponse actualizar(ConductorDTORequest bean,Long id) {
        //OBTENEMOS CONDUCTOR POR ID Y PERSONA
        Conductor conductor = conductorRepository.findById(id).orElseThrow(() -> new NotFoundException("Conductor con ID: " + id + " no encontrado"));
        Persona persona = conductor.getPersona();

        //VALIDAR DUPLICADO DNI
        if(personaRepository.existsByDni(bean.dni()) && !Objects.equals(conductor.getPersona().getDni(), bean.dni())){
            throw new DataExistException("El DNI: " + bean.dni() + " ya está registrado");
        }
        //VALIDAR DUPLICADO TELEFONO
        if(personaRepository.existsByTelefono(bean.telefono()) && !Objects.equals(conductor.getPersona().getTelefono(),bean.telefono())){
            throw new DataExistException("El teléfono: " + bean.telefono() + " ya está registrado");
        }
        //VALIDAR DUPLICADO BREVETE
        if(conductorRepository.existsByBrevete(bean.brevete()) && !Objects.equals(conductor.getBrevete(),bean.brevete())){
            throw new DataExistException("El brevete: " + bean.brevete() + " ya está registrado");
        }

        //ACTUALIZAMOS LOS DATOS DE PERSONA
        persona.setNombre(bean.nombre());
        persona.setApellido(bean.apellido());
        persona.setDni(bean.dni());
        persona.setTelefono(bean.telefono());

        //ACTUALIZAMOS LOS DATOS DE CONDUCTOR
        conductor.setPersona(persona);
        conductor.setBrevete(bean.brevete());
        conductor.setTipoConductor(bean.tipoConductor());
        return entityToResponse(conductorRepository.save(conductor));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConductorDTOResponse> buscarXNombreOBrevete(String texto) {
        //BUSCAR CONDUCTOR X NOMBRE O BREVETE
        return conductorRepository
                .findByPersonaNombreContainingIgnoreCaseOrPersonaApellidoContainingIgnoreCaseOrBreveteContainingIgnoreCase(texto,texto,texto)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        //OBTENEMOS EL CONDUCTOR X ID
        Conductor bean = conductorRepository.findById(id).orElseThrow(()->new NotFoundException("Conductor con ID: " + id + " no encontrado"));
        //DESACTIVAMOS CONDUCTOR
        bean.setActivo(false);
        conductorRepository.save(bean);
    }

    public String actualizarFoto(Long id, MultipartFile foto){

        //OBTENEMOS AL CONDUCTOR
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conductor con ID: " + id + " no encontrado"));
        //VALIDAMOS LA FOTO
        validarFoto(foto);

        //VALIDAMOS QUE NO HAYA FOTO
        if (conductor.getFotoUrl() != null) {
            archivoService.eliminar(conductor.getFotoUrl());
        }

        //SUBIMOS LA FOTO
        String fotoUrl = archivoService.subir(foto);
        conductor.setFotoUrl(fotoUrl);
        conductorRepository.save(conductor);
        return fotoUrl;
    }

    @Override
    @Transactional(readOnly = true)
    public ArchivoContenido obtenerFoto(Long id) {
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conductor con ID: " + id + " no encontrado"));
        if (conductor.getFotoUrl() == null) {
            throw new NotFoundException("El conductor con ID: " + id + " no tiene una fotografía registrada");
        }
        return archivoService.leerFoto(conductor.getFotoUrl());
    }

    private void validarFoto(MultipartFile archivo) {

        if (archivo == null || archivo.isEmpty()) {
            throw new BadRequestException(
                    "La fotografía es obligatoria"
            );
        }

        String contentType = archivo.getContentType();

        if (!List.of(
                "image/jpeg",
                "image/png"
        ).contains(contentType)) {
            throw new BadRequestException(
                    "La fotografía debe ser JPG o PNG"
            );
        }

        if (archivo.getSize() > 5 * 1024 * 1024) {
            throw new BadRequestException(
                    "La fotografía no puede superar los 5 MB"
            );
        }
    }
}
