package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.SecuenciaCodigo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ISecuenciaCodigoRepository extends JpaRepository<SecuenciaCodigo, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SecuenciaCodigo s where s.anio = :anio")
    Optional<SecuenciaCodigo> findByAnioForUpdate(@Param("anio") Integer anio);
}
