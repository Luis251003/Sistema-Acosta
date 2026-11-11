package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_ubigeo_departamentos")
@Getter
@Setter
@NoArgsConstructor
public class UbigeoDepartamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubigeo_departamento", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String departamento;

    @Column(nullable = false, length = 2, unique = true)
    private String ubigeo;
}
