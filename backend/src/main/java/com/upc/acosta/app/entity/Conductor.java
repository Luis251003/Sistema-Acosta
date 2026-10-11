package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "tb_conductores")
@Getter
@Setter
@NoArgsConstructor
public class Conductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conductor")
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String brevete;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conductor", nullable = false, length = 20)
    private TipoConductor tipoConductor;

    @Column(nullable = false)
    private Boolean estado = false;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_persona", nullable = false,unique = true)
    private Persona persona;

    public Conductor(Persona persona, String brevete, TipoConductor tipoConductor) {
        this.persona = persona;
        this.brevete = brevete;
        this.tipoConductor = tipoConductor;
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

    //METODO PARA EVALUAR ESTADO DEL CONDUCTOR
    public Boolean evaluarEstadoConductor(List<TipoDocumentoConductor> tiposObligatorios,
                                          List<DocumentoConductor> documentos,
                                          LocalDate fechaConsulta){
        return tiposObligatorios.stream().allMatch(tipo ->
                documentos.stream().anyMatch(documento ->
                        documento.getTipoDocumentoConductor().equals(tipo)
                                && documento.getFechaVencimiento() != null
                                && !documento.getFechaVencimiento()
                                .isBefore(fechaConsulta)
                )
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Conductor conductor = (Conductor) o;
        return Objects.equals(id, conductor.id) && Objects.equals(brevete, conductor.brevete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brevete);
    }
}
