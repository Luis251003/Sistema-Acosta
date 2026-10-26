package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @Column(name = "direccion_fiscal", length = 300)
    private String direccionFiscal;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado",nullable = false)
    private EstadoContribuyente estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condicion",nullable = false)
    private CondicionContribuyente condicion;

    //METODO CONSTRUCTOR
    public Cliente(String ruc, String razonSocial, String direccionFiscal, EstadoContribuyente estado, CondicionContribuyente condicion){
        this.ruc=ruc;
        this.razonSocial=razonSocial;
        this.direccionFiscal=direccionFiscal;
        this.estado=estado;
        this.condicion=condicion;
    }

    //METODO PARA CONFIGURAR LOS VALORES ANTES DE PERSISTIR
    @PrePersist
    private void configurar(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void configurarAntesDeActualizar() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) && Objects.equals(ruc, cliente.ruc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruc);
    }
}
