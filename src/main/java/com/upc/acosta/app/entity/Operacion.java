package com.upc.acosta.app.entity;

import com.upc.acosta.app.entity.tipos.EstadoOperacion;
import com.upc.acosta.app.entity.tipos.MetodoResolucion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_punto_origen")
    private PuntoOrigen puntoOrigen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_partida", nullable = false)
    private Ubicacion partida;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_destino", nullable = false)
    private Ubicacion destino;

    @OneToOne
    @JoinColumn(name = "id_codigo_operacion", referencedColumnName = "id_codigo_operacion",
            unique = true, nullable = false)
    private CodigoOperacion codigoOperacion;

    @OneToOne(mappedBy = "operacion")
    private Flota flota;

    @Column(name = "fecha_recojo_programado", nullable = false)
    private LocalDate fechaRecojoProgramado;

    @Column(name = "fecha_entrega_programada")
    private LocalDate fechaEntregaProgramada;

    @Column(name = "hora_entrega_programada")
    private LocalTime horaEntregaProgramada;

    @Column(name = "token_seguimiento", unique = true,
            nullable = false, length = 36)
    private String tokenSeguimiento;

    @Column(length = 300)
    private String observacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoOperacion estado = EstadoOperacion.REGISTRADO;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_resolucion", length = 20)  // ALIAS, REGLA, NO_ENCONTRADO
    private MetodoResolucion metodoResolucion;

    @NotNull
    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Operacion(Cliente cliente, Ubicacion partida, Ubicacion destino, CodigoOperacion codigoOperacion, LocalDate fechaRecojoProgramado, String tokenSeguimiento, MetodoResolucion metodoResolucion){
        this.cliente = cliente;
        this.partida = partida;
        this.destino = destino;
        this.codigoOperacion = codigoOperacion;
        this.fechaRecojoProgramado = fechaRecojoProgramado;
        this.tokenSeguimiento = tokenSeguimiento;
        this.metodoResolucion = metodoResolucion;
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
        return Objects.equals(id, operacion.id) && Objects.equals(codigoOperacion, operacion.codigoOperacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigoOperacion);
    }
}
