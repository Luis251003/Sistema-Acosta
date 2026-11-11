package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Operacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IOperacionRepository extends JpaRepository<Operacion, Long>, JpaSpecificationExecutor<Operacion> {
    Optional<Operacion> findByIdAndCodigoOperacion_Usuario_Id(Long id, Long usuarioId);
}
