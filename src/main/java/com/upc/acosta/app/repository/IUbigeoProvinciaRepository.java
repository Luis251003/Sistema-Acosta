package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.UbigeoProvincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUbigeoProvinciaRepository extends JpaRepository<UbigeoProvincia, Long> {
    List<UbigeoProvincia> findByUbigeoDepartamentoId(long idDepartamento);
}
