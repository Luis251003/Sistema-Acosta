package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.DocumentoCamion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDocumentoCamionRepository extends JpaRepository<DocumentoCamion, Long> {
    List<DocumentoCamion> findAllByCamion_Id(Long idCamion);
    List<DocumentoCamion> findAllByCamion_IdOrderByCreatedAtDesc(Long idCamion);
    Optional<DocumentoCamion> findByIdAndCamion_Id(Long id, Long idCamion);
    boolean existsByCamion_IdAndTipoDocumentoCamion_IdTipoDocumentoCamion(
            Long idCamion,
            Long idTipoDocumentoCamion
    );
    boolean existsByCamion_IdAndTipoDocumentoCamion_IdTipoDocumentoCamionAndIdNot(
            Long idCamion,
            Long idTipoDocumentoCamion,
            Long idDocumento
    );
}
