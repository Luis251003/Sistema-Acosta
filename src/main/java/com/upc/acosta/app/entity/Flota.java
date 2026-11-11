package com.upc.acosta.app.entity;

import com.upc.acosta.app.entity.tipos.RolFlota;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_flotas")
@Getter
@Setter
@NoArgsConstructor
public class Flota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_flota")
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_operacion", nullable = false, unique = true)
    private Operacion operacion;

    @OneToMany(mappedBy = "flota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlotaDetalle> detalles = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Flota(Operacion operacion){
        this.operacion = operacion;
    }

    public void agregarDetalle(Conductor conductor, Camion camion, RolFlota rol) {
        detalles.add(new FlotaDetalle(this, conductor, camion, rol));
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
