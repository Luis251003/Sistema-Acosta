package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.CodigoOperacion;
import com.upc.acosta.app.entity.tipos.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICodigoOperacionRepository extends JpaRepository<CodigoOperacion,Long> {
    Optional<CodigoOperacion> findByUsuarioIdAndEstado(Long usuarioId, EstadoReserva estado);
}
