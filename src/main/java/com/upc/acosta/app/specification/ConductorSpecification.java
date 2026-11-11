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

    public static Specification<Conductor> filtrar(
            String busqueda,
            TipoConductor tipo
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (busqueda != null && !busqueda.isBlank()) {

                Join<Conductor, Empleado> empleado =
                        root.join("empleado");

                String[] terminos = busqueda
                        .trim()
                        .toLowerCase()
                        .split("\\s+");

                List<Predicate> coincidencias = new ArrayList<>();

                for (String termino : terminos) {

                    String patron = "%" + termino + "%";

                    Predicate nombre = cb.like(
                            cb.lower(empleado.get("nombre")),
                            patron
                    );

                    Predicate apellido = cb.like(
                            cb.lower(empleado.get("apellido")),
                            patron
                    );

                    Predicate brevete = cb.like(
                            cb.lower(root.get("brevete")),
                            patron
                    );

                    coincidencias.add(
                            cb.or(nombre, apellido, brevete)
                    );
                }

                predicates.add(
                        cb.and(coincidencias.toArray(new Predicate[0]))
                );
            }

            if (tipo != null) {
                predicates.add(
                        cb.equal(root.get("tipoConductor"), tipo)
                );
            }

            predicates.add(
                    cb.isTrue(root.get("activo"))
            );

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
