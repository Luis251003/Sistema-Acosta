package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.CondicionCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICondicionClienteRepository extends JpaRepository<CondicionCliente,Long> {
    CondicionCliente findByNombre(String nombre);
}
