package com.upc.acosta.app.controller;

import com.upc.acosta.app.dto.ClienteDTORequest;
import com.upc.acosta.app.dto.ClienteDTOResponse;
import com.upc.acosta.app.dto.RucDTOResponse;
import com.upc.acosta.app.service.ConsultaRUCService;
import com.upc.acosta.app.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ConsultaRUCService rucService;
    private final IClienteService clienteService;

    public ClienteController(ConsultaRUCService rucService, IClienteService clienteService) {
        this.rucService = rucService;
        this.clienteService = clienteService;
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<?> consultarRUC(@PathVariable String ruc){
        RucDTOResponse response = rucService.consultarRUC(ruc);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> listar(){
        List<ClienteDTOResponse> clientes = clienteService.listar();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarXId(@PathVariable Long id){
        ClienteDTOResponse cliente = clienteService.buscarXId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/buscar/{texto}")
    public ResponseEntity<?> buscar(@PathVariable String texto){
        List<ClienteDTOResponse> clientes = clienteService.buscarXRazonSocialORuc(texto);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody ClienteDTORequest bean){
        ClienteDTOResponse cliente = clienteService.registrar(bean);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable long id, @Valid @RequestBody ClienteDTORequest bean){
        ClienteDTOResponse cliente = clienteService.editar(id,bean);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable long id){
        clienteService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cliente eliminado");
    }
}
