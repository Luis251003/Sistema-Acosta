package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IClienteRepository extends JpaRepository<Cliente,Long> {

    List<Cliente> findByRucContainingOrRazonSocialContainingIgnoreCase(
            String ruc,
            String razonSocial
    );

    Optional<Cliente> findByRuc(String ruc);

    boolean existsByRuc(String ruc);

}
