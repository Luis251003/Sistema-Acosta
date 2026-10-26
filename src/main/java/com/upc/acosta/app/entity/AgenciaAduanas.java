package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "tb_agencias_aduanas")
@Getter
@Setter
@NoArgsConstructor
public class AgenciaAduanas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agencia_aduana")
    private Long id;

    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado",nullable = false)
    private EstadoContribuyente estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condicion",nullable = false)
    private CondicionContribuyente condicion;

    public AgenciaAduanas(String ruc, String razonSocial, EstadoContribuyente estado, CondicionContribuyente condicion) {
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.estado = estado;
        this.condicion = condicion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AgenciaAduanas remitente = (AgenciaAduanas) o;
        return Objects.equals(id, remitente.id) && Objects.equals(ruc, remitente.ruc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruc);
    }
}
