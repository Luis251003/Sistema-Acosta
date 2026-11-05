package com.upc.acosta.app.specification;

import com.upc.acosta.app.entity.Camion;
import com.upc.acosta.app.entity.tipos.TipoVehiculo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CamionSpecification {
    public static Specification<Camion> filtrar(String busqueda, TipoVehiculo tipoVehiculo){
        System.out.println(busqueda);
        return(root,query,cb)->{
            List<Predicate> predicates = new ArrayList<>();

            //Filtrar por placa
            if(busqueda != null && !busqueda.isBlank()){
                String termino = "%"+busqueda.trim().toLowerCase()+"%";
                predicates.add(cb.like(cb.lower(root.get("placa")),termino));
            }

            //Filtrar por tipo de camion
            if(tipoVehiculo != null){
                predicates.add(cb.equal(root.get("tipoVehiculo"),tipoVehiculo));
            }

            //Filtrar por estado activo
            predicates.add(cb.isTrue(root.get("activo")));

            //Guardamos los filtros
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
