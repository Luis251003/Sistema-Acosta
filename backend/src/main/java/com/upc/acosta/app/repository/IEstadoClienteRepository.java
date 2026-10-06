package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.EstadoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoClienteRepository extends JpaRepository<EstadoCliente,Long> {
    EstadoCliente findByNombre(String nombre);
}
