package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Operacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOperacionRepository extends JpaRepository<Operacion, Long> {
}
