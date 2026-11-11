package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Flota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface IFlotaRepository extends JpaRepository<Flota, Long> {
    boolean existsByOperacion_Id(Long idOperacion);
    Optional<Flota> findByOperacion_Id(Long idOperacion);

    @Query("""
            select case when count(fd) > 0 then true else false end
            from FlotaDetalle fd
            join fd.flota f
            join f.operacion o
            where fd.conductor.id = :conductorId
              and o.fechaRecojoProgramado = :fechaRecojo
              and o.id <> :operacionId
              and o.activo = true
              and o.estado <> com.upc.acosta.app.entity.tipos.EstadoOperacion.CANCELADO
            """)
    boolean existsAsignacionConductorEnFecha(
            @Param("conductorId") Long conductorId,
            @Param("fechaRecojo") LocalDate fechaRecojo,
            @Param("operacionId") Long operacionId
    );

    @Query("""
            select case when count(fd) > 0 then true else false end
            from FlotaDetalle fd
            join fd.flota f
            join f.operacion o
            where fd.camion.id = :camionId
                and o.fechaRecojoProgramado = :fechaRecojo
                and o.id <> :operacionId
                and o.activo = true
                and o.estado <> com.upc.acosta.app.entity.tipos.EstadoOperacion.CANCELADO
            """)
    boolean existsAsignacionCamionEnFecha(
            @Param("camionId") Long camionId,
            @Param("fechaRecojo") LocalDate fechaRecojo,
            @Param("operacionId") Long operacionId
    );
}
