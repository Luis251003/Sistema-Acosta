package com.upc.acosta.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "tb_operaciones",
        uniqueConstraints = @UniqueConstraint(name = "uk_tb_operaciones_codigo", columnNames = "codigo")
)
@Getter
@Setter
@NoArgsConstructor
public class Operacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_agencia_aduana", nullable = false)
    private AgenciaAduanas agenciaAduanas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_partida", nullable = false)
    private Ubicacion partida;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_destino", nullable = false)
    private Ubicacion destino;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_punto_origen")
    private PuntoOrigen puntoOrigen;

    @Column(length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "timestamp_recojo_programado", nullable = false)
    private LocalDateTime recojoProgramado;

    @Column(name = "timestamp_entrega_programada")
    private LocalDateTime entregaProgramado;

    @Column(name = "token_seguimiento", unique = true,
            nullable = false, length = 36)
    private String tokenSeguimiento;

    @Column
    private String observacion;

    @Column(nullable = false, length = 30)
    private String estado;

    @NotNull
    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Operacion(AgenciaAduanas agenciaAduanas, Cliente cliente, Ubicacion partida, Ubicacion destino, PuntoOrigen puntoOrigen) {
        this.agenciaAduanas = agenciaAduanas;
        this.cliente = cliente;
        this.partida = partida;
        this.destino = destino;
        this.puntoOrigen = puntoOrigen;
    }

    @PrePersist
    private void configurarAntesDePersistir() {
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
        Operacion operacion = (Operacion) o;
        return Objects.equals(id, operacion.id) && Objects.equals(codigo, operacion.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
}
