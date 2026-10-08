package com.upc.acosta.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_tipo_documento_conductor")
@Getter
@Setter
@NoArgsConstructor
public class TipoDocumentoConductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento_conductor")
    private Long idTipoDocumentoConductor;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    public TipoDocumentoConductor(String nombre) {
        this.nombre = nombre;
    }
}
