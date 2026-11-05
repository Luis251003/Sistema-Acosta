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
        name = "tb_flotas",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_tb_flota_operacion_conductor",
                columnNames = {"id_operacion", "id_conductor"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Flota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_flota")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_operacion", nullable = false)
    private Operacion operacion;

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

    public Flota(Operacion operacion, Conductor conductor, Camion camion, RolFlota rol){
        this.operacion = operacion;
        this.conductor = conductor;
        this.camion = camion;
        this.rol = rol;
    }

    @PrePersist
    private void configurar() {
        LocalDateTime ahora = LocalDateTime.now();
        this.createdAt = ahora;
        this.updatedAt = ahora;
    }

    @PreUpdate
    private void configurarAntesDeActualizar() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Flota flota = (Flota) o;
        return Objects.equals(id, flota.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
