package com.upc.acosta.app.entity;

import com.upc.acosta.app.entity.tipos.RolFlota;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "tb_flotas_detalle",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tb_flotas_detalle_flota_conductor",
                        columnNames = {"id_flota", "id_conductor"}
                ),
                @UniqueConstraint(
                        name = "uk_tb_flotas_detalle_flota_rol",
                        columnNames = {"id_flota", "rol_flota"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FlotaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_flota_detalle")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_flota", nullable = false)
    private Flota flota;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_conductor", nullable = false)
    private Conductor conductor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_camion", nullable = false)
    private Camion camion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rol_flota", nullable = false, length = 20)
    private RolFlota rol;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public FlotaDetalle(Flota flota, Conductor conductor, Camion camion, RolFlota rol) {
        this.flota = flota;
        this.conductor = conductor;
        this.camion = camion;
        this.rol = rol;
    }

    @PrePersist
    private void configurar() {
        LocalDateTime ahora = LocalDateTime.now();
        createdAt = ahora;
        updatedAt = ahora;
    }

    @PreUpdate
    private void configurarAntesDeActualizar() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlotaDetalle that = (FlotaDetalle) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
