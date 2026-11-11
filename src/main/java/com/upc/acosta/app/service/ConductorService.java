package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.ConductorDTORequest;
import com.upc.acosta.app.dto.ConductorDTOResponse;
import com.upc.acosta.app.dto.ConductorSeleccionadoDTOResponse;
import com.upc.acosta.app.dto.TipoDocumentoDTOResponse;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.entity.tipos.RolDocumento;
import com.upc.acosta.app.entity.tipos.TipoConductor;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.IConductorRepository;
import com.upc.acosta.app.repository.IEmpleadoRepository;
import com.upc.acosta.app.repository.IFlotaRepository;
import com.upc.acosta.app.repository.IOperacionRepository;
import com.upc.acosta.app.repository.ITipoDocumentoRepository;
import com.upc.acosta.app.service.interfaces.IArchivoService;
import com.upc.acosta.app.service.interfaces.IConductorService;
import com.upc.acosta.app.service.interfaces.IDocumentoService;
import com.upc.acosta.app.specification.ConductorSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConductorService implements IConductorService {

    //REPOSITORIOS
    private final IConductorRepository conductorRepository;
    private final IEmpleadoRepository personaRepository;
    private final IOperacionRepository operacionRepository;
    private final IFlotaRepository flotaRepository;

    //SERVICIOS
    private final ITipoDocumentoRepository iTipoDocumentoRepository;
    private final IArchivoService archivoService;
    private final IDocumentoService documentoService;

    //METODO DE MAPEO
    private ConductorDTOResponse entityToResponse(Conductor bean){
        //ID DE EMPLEADO
        long id = bean.getEmpleado().getId();
        //OBTENEMOS AL EMPLEADO
        Empleado empleado = personaRepository.findById(bean.getEmpleado().getId()).orElseThrow(()->new NotFoundException("Empleado con ID: " + id + "no encontrado"));
        return new ConductorDTOResponse(
                bean.getId(),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getTelefono(),
                empleado.getDni(),
                bean.getBrevete(),
                bean.getFotoUrl(),
                bean.getTipoConductor(),
                bean.getHabilitado()
        );
    }

    private ConductorSeleccionadoDTOResponse conductorSeleccionadoToResponse(ConductorDTOResponse datos,boolean disponible){
        return new ConductorSeleccionadoDTOResponse(
                datos.id(),
                datos.nombre(),
                datos.apellido(),
                datos.telefono(),
                datos.dni(),
                datos.brevete(),
                datos.fotoUrl(),
                datos.tipoConductor(),
                datos.habilitado(),
                disponible
        );
    }

    private TipoDocumentoDTOResponse entityTipoDocumentoToResponse(TipoDocumento bean){
        return new TipoDocumentoDTOResponse(
                bean.getIdTipoDocumento(),
                bean.getNombre(),
                bean.getRol().toString()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConductorDTOResponse> listar(String busqueda, TipoConductor tipoConductor) {
        Specification<Conductor> spec = ConductorSpecification.filtrar(busqueda,tipoConductor);
        return conductorRepository.findAll(spec)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Set<TipoDocumentoDTOResponse> listarTiposDocumento() {
        return iTipoDocumentoRepository.findByRol(RolDocumento.CONDUCTOR).stream().map(this::entityTipoDocumentoToResponse).collect(Collectors.toSet());
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
        Empleado empleado = personaRepository.save(new Empleado(bean.nombre(),bean.apellido(),bean.telefono(),bean.dni()));
        //CREAR CONDUCTOR
        Conductor conductor = new Conductor(empleado, bean.brevete(), bean.tipoConductor());

        return entityToResponse(conductorRepository.save(conductor));
    }

    @Override
    @Transactional
    public ConductorDTOResponse actualizar(ConductorDTORequest bean,Long id) {
        //OBTENEMOS CONDUCTOR POR ID Y PERSONA
        Conductor conductor = conductorRepository.findById(id).orElseThrow(() -> new NotFoundException("Conductor con ID: " + id + " no encontrado"));
        Empleado empleado = conductor.getEmpleado();

        //VALIDAR DUPLICADO DNI
        if(personaRepository.existsByDni(bean.dni()) && !Objects.equals(conductor.getEmpleado().getDni(), bean.dni())){
            throw new DataExistException("El DNI: " + bean.dni() + " ya está registrado");
        }
        //VALIDAR DUPLICADO TELEFONO
        if(personaRepository.existsByTelefono(bean.telefono()) && !Objects.equals(conductor.getEmpleado().getTelefono(),bean.telefono())){
            throw new DataExistException("El teléfono: " + bean.telefono() + " ya está registrado");
        }
        //VALIDAR DUPLICADO BREVETE
        if(conductorRepository.existsByBrevete(bean.brevete()) && !Objects.equals(conductor.getBrevete(),bean.brevete())){
            throw new DataExistException("El brevete: " + bean.brevete() + " ya está registrado");
        }

        //ACTUALIZAMOS LOS DATOS DE PERSONA
        empleado.setNombre(bean.nombre());
        empleado.setApellido(bean.apellido());
        empleado.setDni(bean.dni());
        empleado.setTelefono(bean.telefono());

        //ACTUALIZAMOS LOS DATOS DE CONDUCTOR
        conductor.setEmpleado(empleado);
        conductor.setBrevete(bean.brevete());
        conductor.setTipoConductor(bean.tipoConductor());
        return entityToResponse(conductorRepository.save(conductor));
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

    // SERVICIOS DE BUSQUEDA

    @Override
    public ConductorSeleccionadoDTOResponse buscarXIdAndDisponibilidad(Long id, Long idOperacion) {
        //Actualizamos el estado de habilitacion del conductor
        documentoService.actualizarEstadosDocumentos(id,RolDocumento.CONDUCTOR);
        //Obtenemos el conductor actualizado
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conductor con ID: " + id + " no encontrado"));
        return buscarDisponibilidad(conductor,idOperacion);
    }

    @Override
    public ConductorSeleccionadoDTOResponse buscarXBreveteAndDisponibilidad(String brevete, Long idOperacion) {

        Conductor conductor = conductorRepository.findByBrevete(brevete.trim())
                .orElseThrow(() -> new NotFoundException("Conductor con brevete: " + brevete + " no encontrado"));
        documentoService.actualizarEstadosDocumentos(conductor.getId(), RolDocumento.CONDUCTOR);
        Conductor conductorSaved = conductorRepository.findById(conductor.getId())
                .orElseThrow(() -> new NotFoundException("Conductor con ID: " + conductor.getId() + " no encontrado"));
        return buscarDisponibilidad(conductorSaved,idOperacion);
    }

    // SERVICIOS DE FOTO

    @Override
    @Transactional
    public void actualizarFoto(Long id, MultipartFile foto){

        //OBTENEMOS AL CONDUCTOR
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conductor con ID: " + id + " no encontrado"));
        //VALIDAMOS LA FOTO
        validarFoto(foto);

        //SUBIMOS LA FOTO
        String fotoKey = archivoService.subirFoto(foto);

        //VALIDAMOS QUE NO HAYA FOTO
        if (conductor.getFotoUrl() != null) {
            archivoService.eliminarArchivo(conductor.getFotoUrl());
        }

        //GUARDAMOS EN LA BASE DE DATOS
        conductor.setFotoUrl(fotoKey);
        conductorRepository.save(conductor);
    }

    public String obtenerURLFoto(Long id) {
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conductor con ID " + id + " no encontrado"));

        String key = conductor.getFotoUrl();

        if (key == null || key.isBlank()) {
            throw new NotFoundException(
                    "El conductor no tiene foto"
            );
        }

        return archivoService.obtenerUrlTemporal(key).toString();
    }

    private ConductorSeleccionadoDTOResponse buscarDisponibilidad(Conductor conductor,Long idOperacion){

        Operacion operacion = operacionRepository.findById(idOperacion)
                .orElseThrow(() -> new NotFoundException("Operación con ID: " + idOperacion + " no encontrada"));

        LocalDate fechaRecojo = operacion.getFechaRecojoProgramado();
        if (fechaRecojo == null) {
            throw new BadRequestException(
                    "La operación con ID: " + idOperacion + " no tiene una fecha de entrega programada"
            );
        }

        boolean disponible = !flotaRepository.existsAsignacionConductorEnFecha(
                        conductor.getId(),
                        fechaRecojo,
                        operacion.getId()
                );
        ConductorDTOResponse datos = entityToResponse(conductor);
        return conductorSeleccionadoToResponse(datos,disponible);

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
