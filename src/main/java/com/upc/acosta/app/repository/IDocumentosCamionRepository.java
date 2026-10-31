package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.DocumentoCamion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDocumentosCamionRepository extends JpaRepository<DocumentoCamion, Long> {
    Optional<List<DocumentoCamion>> findByCamionId(Long camionId);
    Optional<DocumentoCamion> findByIdAndCamionId(Long id, Long camionId);
}
