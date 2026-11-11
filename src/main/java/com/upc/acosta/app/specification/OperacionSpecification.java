package com.upc.acosta.app.specification;

import com.upc.acosta.app.entity.Camion;
import com.upc.acosta.app.entity.Cliente;
import com.upc.acosta.app.entity.Conductor;
import com.upc.acosta.app.entity.Empleado;
import com.upc.acosta.app.entity.Flota;
import com.upc.acosta.app.entity.FlotaDetalle;
import com.upc.acosta.app.entity.Operacion;
import com.upc.acosta.app.entity.tipos.EstadoOperacion;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OperacionSpecification {

    public static Specification<Operacion> filtrar(
            String busqueda,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            List<String> placas,
            List<EstadoOperacion> estados
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (busqueda != null && !busqueda.isBlank()) {
                Join<Operacion, Cliente> cliente = root.join("cliente", JoinType.LEFT);
                Join<Operacion, Flota> flota = root.join("flota", JoinType.LEFT);
                Join<Flota, FlotaDetalle> detalle = flota.join("detalles", JoinType.LEFT);
                Join<FlotaDetalle, Conductor> conductor = detalle.join("conductor", JoinType.LEFT);
                Join<Conductor, Empleado> empleado = conductor.join("empleado", JoinType.LEFT);

                List<Predicate> coincidenciasCliente = new ArrayList<>();
                List<Predicate> coincidenciasConductor = new ArrayList<>();

                for (String termino : busqueda.trim().toLowerCase(Locale.ROOT).split("\\s+")) {
                    String patron = "%" + termino + "%";
                    coincidenciasCliente.add(
                            cb.like(cb.lower(cliente.get("razonSocial")), patron)
                    );
                    coincidenciasConductor.add(
                            cb.or(
                                    cb.like(cb.lower(empleado.get("nombre")), patron),
                                    cb.like(cb.lower(empleado.get("apellido")), patron)
                            )
                    );
                }

                predicates.add(
                        cb.or(
                                cb.and(coincidenciasCliente.toArray(new Predicate[0])),
                                cb.and(coincidenciasConductor.toArray(new Predicate[0]))
                        )
                );
                query.distinct(true);
            }

            if (fechaInicio != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("fechaRecojoProgramado"), fechaInicio)
                );
            }

            if (fechaFin != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("fechaRecojoProgramado"), fechaFin)
                );
            }

            List<String> placasNormalizadas = placas == null
                    ? List.of()
                    : placas.stream()
                            .filter(Objects::nonNull)
                            .map(String::trim)
                            .filter(placa -> !placa.isEmpty())
                            .map(placa -> placa.toUpperCase(Locale.ROOT))
                            .toList();
            if (!placasNormalizadas.isEmpty()) {
                Join<Operacion, Flota> flota = root.join("flota", JoinType.LEFT);
                Join<Flota, FlotaDetalle> detalle = flota.join("detalles", JoinType.LEFT);
                Join<FlotaDetalle, Camion> camion = detalle.join("camion", JoinType.LEFT);
                predicates.add(
                        cb.upper(camion.get("placa")).in(placasNormalizadas)
                );
                query.distinct(true);
            }

            List<EstadoOperacion> estadosValidos = estados == null
                    ? List.of()
                    : estados.stream().filter(Objects::nonNull).toList();
            if (!estadosValidos.isEmpty()) {
                predicates.add(root.get("estado").in(estadosValidos));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
