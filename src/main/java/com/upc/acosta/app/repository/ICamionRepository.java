package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ICamionRepository extends JpaRepository<Camion, Long>, JpaSpecificationExecutor<Camion> {
    boolean existsByPlacaIgnoreCase(String placa);
    boolean existsByPlacaIgnoreCaseAndIdNot(String placa, Long id);
    boolean existsByNumeroTuceIgnoreCase(String numeroTuce);
    boolean existsByNumeroTuceIgnoreCaseAndIdNot(String numeroTuce, Long id);

    List<Camion> findByPlacaContainingIgnoreCase(String placa);
}
