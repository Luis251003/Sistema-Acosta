package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.ClienteDTORequest;
import com.upc.acosta.app.dto.ClienteDTOResponse;
import com.upc.acosta.app.dto.ClienteUpdateDTORequest;
import com.upc.acosta.app.entity.*;
import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import com.upc.acosta.app.repository.IClienteRepository;
import com.upc.acosta.app.repository.ICondicionContribuyenteRepository;
import com.upc.acosta.app.repository.IEstadoContribuyenteRepository;
import com.upc.acosta.app.validator.RucValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService implements IClienteService{

    //REPOSITORIO
    private final IClienteRepository clienteRepository;
    private final ICondicionContribuyenteRepository condicionContribuyenteRepository;
    private final IEstadoContribuyenteRepository estadoContribuyenteRepository;

    //METODO CONSTRUCTOR
    public ClienteService(
            IClienteRepository clienteRepository,
            ICondicionContribuyenteRepository condicionRepository,
            IEstadoContribuyenteRepository estadoRepository) {
        this.clienteRepository = clienteRepository;
        this.condicionContribuyenteRepository = condicionRepository;
        this.estadoContribuyenteRepository = estadoRepository;
    }

    //METODOS DE CONVERSION
    private ClienteDTOResponse entityToResponse(Cliente bean){
        return new ClienteDTOResponse(
                bean.getId(),
                bean.getRazonSocial(),
                bean.getRuc(),
                bean.getEstado().getNombre(),
                bean.getCondicion().getNombre(),
                bean.getDireccionFiscal()
        );
    }

    private Cliente requestToEntity(ClienteDTORequest bean){
        //Buscamos la condición
        CondicionContribuyente condicion = condicionContribuyenteRepository.findById(bean.id_condicion()).orElseThrow(()->new NotFoundException("La condición con ID: " + bean.id_condicion() + " no ha sido encontrado"));
        //Buscamos el estado
        EstadoContribuyente estado = estadoContribuyenteRepository.findById(bean.id_estado()).orElseThrow(()->new NotFoundException("El estado con ID: " + bean.id_estado() + " no ha sido encontrado"));
        //Creamos al cliente
        return new Cliente(bean.ruc(),bean.razonSocial(),bean.direccion(),estado,condicion);

    }

    //METODOS CRUD
    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTOResponse> listar() {
        return clienteRepository.findAll()
                .stream()
                .filter((x)->x.getActivo()==true)
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTOResponse buscarXId(Long id) {
        return clienteRepository.findById(id).map(this::entityToResponse).orElseThrow(()->new NotFoundException("Error al encontrar el cliente"));
    }

    @Override
    @Transactional
    public ClienteDTOResponse registrar(ClienteDTORequest cliente) {

        //VALIDAMOS QUE EL RUC NO EXISTA
        if(clienteRepository.existsByRuc(cliente.ruc())){
            throw new DataExistException("El RUC " + cliente.ruc() + " ya existe");
        }
        //VALIDAMOS QUE EL RUC SEA VALIDO
        if (!RucValidator.validar(cliente.ruc())) {
            throw new BadRequestException("El RUC ingresado no es válido");
        }

        return entityToResponse(clienteRepository.save(requestToEntity(cliente)));
    }

    @Override
    @Transactional
    public ClienteDTOResponse editar(long id, ClienteUpdateDTORequest bean) {
        //VALIDACIONES
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cliente con ID: " + id + " no fue encontrado"));

        CondicionContribuyente condicion = condicionContribuyenteRepository.findById(bean.id_condicion())
                .orElseThrow(() -> new NotFoundException("Condición con ID: " + bean.id_condicion() + " no fue encontrada"));

        EstadoContribuyente estado = estadoContribuyenteRepository.findById(bean.id_estado())
                .orElseThrow(() -> new NotFoundException("Estado con ID: " + bean.id_estado() + " no fue encontrado"));

        //ACTUALIZAMOS LOS ATRIBUTOS
        cliente.setRazonSocial(bean.razonSocial());
        cliente.setDireccionFiscal(bean.direccion());
        cliente.setCondicion(condicion);
        cliente.setEstado(estado);

        //ACTUALIZAMOS AL CLIENTE
        return entityToResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public void eliminar(long id) {
        //VALIDACIONES
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cliente con ID: " + id + " no fue encontrado"));
        //DESACTIVAMOS EL ESTADO ACTIVO
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTOResponse> buscarXRazonSocialORuc(String texto) {
        if(texto == null || texto.trim().isEmpty()) return List.of();
        return clienteRepository.findByRucContainingOrRazonSocialContainingIgnoreCase(texto.trim(),texto.trim()).stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}
