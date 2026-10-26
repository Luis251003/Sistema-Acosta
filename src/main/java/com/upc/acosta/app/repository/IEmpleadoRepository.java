package com.upc.acosta.app.repository;

import com.upc.acosta.app.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpleadoRepository extends JpaRepository<Empleado,Long> {
    //Validar persona X telefono
    Boolean existsByTelefono(String telefono);
    //Validar persona X dni
    Boolean existsByDni(String dni);
}
