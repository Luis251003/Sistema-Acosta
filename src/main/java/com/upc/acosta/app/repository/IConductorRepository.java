package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IConductorRepository extends JpaRepository<Conductor,Long>, JpaSpecificationExecutor<Conductor> {

    //VALIDAR CONDUCTOR X BREVETE
    Boolean existsByBrevete(String brevete);

    //LISTAR TODOS LOS IDS
    @Query("select c.id from Conductor c")
    List<Long> findAllIds();

    //BUSCAR CONDUCTOR POR BREVETE
    Optional<Conductor> findByBrevete(String brevete);

}
