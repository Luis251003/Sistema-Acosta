package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IConductorRepository extends JpaRepository<Conductor,Long>, JpaSpecificationExecutor<Conductor> {
    //VALIDAR CONDUCTOR X BREVETE
    Boolean existsByBrevete(String brevete);
    //BUSCAR CONDUCTOR X NOMBRE O BREVETE
    List<Conductor> findByEmpleadoNombreContainingIgnoreCaseOrEmpleadoApellidoContainingIgnoreCaseOrBreveteContainingIgnoreCase(
            String nombre,
            String apellido,
            String brevete
    );
}
