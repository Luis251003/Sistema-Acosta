package com.upc.acosta.app.specification;

import com.upc.acosta.app.entity.Conductor;
import com.upc.acosta.app.entity.Empleado;
import com.upc.acosta.app.entity.tipos.TipoConductor;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ConductorSpecification {

    public static Specification<Conductor> filtrar(String busqueda, TipoConductor tipo){
        return(root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            //Filtrar por nombre, apellido o brevete
            if(busqueda != null && !busqueda.isBlank()){
                String termino = "%"+busqueda.toLowerCase()+"%";

                Join<Conductor, Empleado> empleado = root.join("empleado");

                Predicate nombre = cb.like(
                        cb.lower(empleado.get("nombre")), termino
                );

                Predicate apellido = cb.like(
                        cb.lower(empleado.get("apellido")), termino
                );
                Predicate brevete = cb.like(cb.lower(root.get("brevete")),termino);

                predicates.add(cb.or(nombre,apellido,brevete));
            }

            //Filtrar por tipo de conductor
            if(tipo != null){
                predicates.add(cb.equal(root.get("tipoConductor"),tipo));
            }

            //Filtrar por estado activo
            predicates.add(cb.isTrue(root.get("activo")));

            //Guardamos los filtros
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
