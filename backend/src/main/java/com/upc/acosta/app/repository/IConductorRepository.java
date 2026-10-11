package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    //ACTUALIZAR ESTADO DEL CONDUCTOR
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE tb_conductores c SET c.estado = :estado WHERE c.id = :id")
    int actualizarEstadoPorId(@Param("id") Long id, @Param("estado") Boolean estado);
}
