package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_tipo_documento")
@Getter
@Setter
@NoArgsConstructor
public class TipoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Long idTipoDocumento;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RolDocumento rol;

    public TipoDocumento(String nombre, RolDocumento rol) {
        this.nombre = nombre;
        this.rol = rol;
    }
}
