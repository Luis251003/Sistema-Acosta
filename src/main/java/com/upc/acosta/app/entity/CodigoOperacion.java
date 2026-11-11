package com.upc.acosta.app.entity;

import com.upc.acosta.app.entity.tipos.EstadoReserva;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_codigos_operacion", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo"}))
public class CodigoOperacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_codigo_operacion")
    private Long id;

    @Column(nullable = false, length = 14)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

}
