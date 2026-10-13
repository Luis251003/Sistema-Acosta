package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICamionRepository extends JpaRepository<Camion, Long> {
    boolean existsByPlacaIgnoreCase(String placa);
    boolean existsByPlacaIgnoreCaseAndIdNot(String placa, Long id);
    boolean existsByNumeroTuceIgnoreCase(String numeroTuce);
    boolean existsByNumeroTuceIgnoreCaseAndIdNot(String numeroTuce, Long id);

    List<Camion> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCaseOrPlacaContainingIgnoreCase(
            String marca,
            String modelo,
            String placa
    );
}
