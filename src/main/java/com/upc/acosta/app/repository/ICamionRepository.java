package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICamionRepository extends JpaRepository<Camion, Long>, JpaSpecificationExecutor<Camion> {
    boolean existsByPlacaIgnoreCase(String placa);
    boolean existsByPlacaIgnoreCaseAndIdNot(String placa, Long id);
    boolean existsByNumeroTuceIgnoreCase(String numeroTuce);
    boolean existsByNumeroTuceIgnoreCaseAndIdNot(String numeroTuce, Long id);

    List<Camion> findByPlacaContainingIgnoreCase(String placa);
    Optional<Camion> findByPlaca(String placa);

    @Query("select c.id from Camion c")
    List<Long> findAllIds();
}
