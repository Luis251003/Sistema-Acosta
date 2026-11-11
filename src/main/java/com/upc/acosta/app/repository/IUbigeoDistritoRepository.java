package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.UbigeoDistrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUbigeoDistritoRepository extends JpaRepository<UbigeoDistrito, Long> {
    List<UbigeoDistrito> findByUbigeoProvinciaId(long idProvincia);
    Optional<UbigeoDistrito> findByUbigeo(String ubigeo);
}
