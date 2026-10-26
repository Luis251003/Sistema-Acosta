package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDocumentoRepository extends JpaRepository<Documento, Long> {
}
