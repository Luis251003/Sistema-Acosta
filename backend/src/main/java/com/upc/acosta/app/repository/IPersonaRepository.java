package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPersonaRepository extends JpaRepository<Persona,Long> {
    //Validar persona X telefono
    Boolean existsByTelefono(String telefono);
    //Validar persona X dni
    Boolean existsByDni(String dni);
}
