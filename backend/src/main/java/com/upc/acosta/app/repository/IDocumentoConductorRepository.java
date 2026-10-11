package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.DocumentoConductor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDocumentoConductorRepository extends JpaRepository<DocumentoConductor, Long> {
    List<DocumentoConductor> findAllByConductorIdOrderByCreatedAtDesc(Long idConductor);
    Optional<DocumentoConductor> findByIdAndConductorId(Long id, Long idConductor);
    List<DocumentoConductor> findAllByConductorId(Long idConductor);
    boolean existsByConductor_IdAndTipoDocumentoConductor_IdTipoDocumentoConductor(
            Long idConductor,
            Long idTipoDocumentoConductor
    );
    boolean existsByConductor_IdAndTipoDocumentoConductor_IdTipoDocumentoConductorAndIdNot(
            Long idConductor,
            Long idTipoDocumentoConductor,
            Long idDocumento
    );
}
