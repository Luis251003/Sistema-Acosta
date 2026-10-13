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
@Table(name = "tb_tipo_documento_camion")
@Getter
@Setter
@NoArgsConstructor
public class TipoDocumentoCamion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento_camion")
    private Long idTipoDocumentoCamion;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    public TipoDocumentoCamion(String nombre) {
        this.nombre = nombre;
    }
}
