package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_eventos_operacion")
public class EventoOperacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento_operacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operacion")
    private Operacion operacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento", nullable = false, length = 20)
    private TipoEvento tipoEvento;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public EventoOperacion(Operacion operacion, TipoEvento tipoEvento, LocalDateTime timestamp, String observacion) {
        this.operacion = operacion;
        this.tipoEvento = tipoEvento;
        this.timestamp = timestamp;
        this.observacion = observacion;
    }

    //METODO PARA CONFIGURAR LOS VALORES ANTES DE PERSISTIR
    @PrePersist
    private void configurar(){
        this.createdAt = LocalDateTime.now();
    }
}
