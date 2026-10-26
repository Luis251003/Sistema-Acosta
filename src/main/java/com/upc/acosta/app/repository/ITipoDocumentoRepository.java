package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
}
