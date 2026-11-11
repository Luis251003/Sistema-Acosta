package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.PuntoOrigenAlias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPuntoOrigenAliasRepository extends JpaRepository<PuntoOrigenAlias,Long> {
    //BUSCAMOS EL PUNTO DE ORIGEN ALIAS POR LA DIRECCION
    Optional<PuntoOrigenAlias> findByDireccionNormalizada(String direccionNormalizada);
}
