package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUbicacionRepository extends JpaRepository<Ubicacion,Long>{
    Optional<Ubicacion> findByDireccionAndUbigeoUbigeo(String direccion,String ubigeo);
}
