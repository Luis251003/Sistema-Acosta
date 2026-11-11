package com.upc.acosta.app.entity;

import com.upc.acosta.app.entity.tipos.Canal;
import com.upc.acosta.app.entity.tipos.Modalidad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_despachos_aduaneros")
public class DespachoAduanero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho_aduanero")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    private Canal canal;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Modalidad modalidad;

    @Column(name = "numero_contenedor",length = 11)
    private String numeroContenedor;

    @Column(precision = 10, scale = 2)
    private BigDecimal peso;

    @Column(nullable = false, length = 15)
    private String estado = "PENDIENTE";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operacion", nullable = false, unique = true)
    private Operacion operacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_linea_naviera")
    private LineaNaviera lineaNaviera;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_agencia_aduana", nullable = false)
    private AgenciaAduanas agenciaAduanas;

    @Column(name = "hora_recojo_programado")
    private LocalTime horaRecojoProgramado;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    //METODO CONSTRUCTOR
    public DespachoAduanero(Canal canal, Modalidad modalidad, String numeroContenedor, BigDecimal peso, Operacion operacion, LineaNaviera lineaNaviera, AgenciaAduanas agenciaAduanas) {
        this.canal = canal;
        this.modalidad = modalidad;
        this.numeroContenedor = numeroContenedor;
        this.peso = peso;
        this.operacion = operacion;
        this.lineaNaviera = lineaNaviera;
        this.agenciaAduanas = agenciaAduanas;
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

    //METODOS DE EQUALS Y HASHCODE

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DespachoAduanero that = (DespachoAduanero) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
