package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "tb_despachos_extracciones")
@Getter
@Setter
@NoArgsConstructor
public class DespachoExtraccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho_extraccion")
    private Long id;
    @Column(name = "campo",nullable = false)
    private String campo;
    @Column(name = "valor_extraido",length = 100)
    private String valorExtraido;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_despacho_aduanero")
    private DespachoAduanero despachoAduanero;
    @ManyToOne
    @JoinColumn(name = "id_documento")
    private DocumentoDespacho documento;

    public DespachoExtraccion(String campo, String valorExtraido, DespachoAduanero despachoAduanero, DocumentoDespacho documento){
        this.campo = campo;
        this.valorExtraido = valorExtraido;
        this.despachoAduanero = despachoAduanero;
        this.documento = documento;
    }

    @PrePersist
    private void configurar() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DespachoExtraccion that = (DespachoExtraccion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
