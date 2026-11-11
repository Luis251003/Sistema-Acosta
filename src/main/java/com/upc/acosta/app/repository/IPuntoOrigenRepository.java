package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.PuntoOrigen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPuntoOrigenRepository extends JpaRepository<PuntoOrigen,Long> {
    Optional<PuntoOrigen> findByNombre(String nombre);
}
