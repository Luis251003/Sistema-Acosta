package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IClienteRepository extends JpaRepository<Cliente,Long> {

    List<Cliente> findByRucContainingOrRazonSocialContainingIgnoreCase(
            String ruc,
            String razonSocial
    );

    boolean existsByRuc(String ruc);

}
