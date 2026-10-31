package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.RolDocumento;
import com.upc.acosta.app.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ITipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
    Set<TipoDocumento> findByRol(RolDocumento rol);
}
