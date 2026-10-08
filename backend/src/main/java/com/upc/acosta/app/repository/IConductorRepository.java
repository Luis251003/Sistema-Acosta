package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IConductorRepository extends JpaRepository<Conductor,Long> {
    //VALIDAR CONDUCTOR X BREVETE
    Boolean existsByBrevete(String brevete);
    //BUSCAR CONDUCTOR X NOMBRE O BREVETE
    List<Conductor> findByPersonaNombreContainingIgnoreCaseOrPersonaApellidoContainingIgnoreCaseOrBreveteContainingIgnoreCase(
            String nombre,
            String apellido,
            String brevete
    );
}
