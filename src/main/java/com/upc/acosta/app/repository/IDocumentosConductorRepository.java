package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.DocumentoConductor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDocumentosConductorRepository extends JpaRepository<DocumentoConductor, Long> {
    List<DocumentoConductor> findByConductorId(Long conductorId);
    Optional<DocumentoConductor> findByIdAndConductorId(Long idDocumento, Long idEntidad);
    boolean existsByConductorIdAndTipoDocumentoIdTipoDocumento(Long idEntidad, Long idTipoDocumento);
}
