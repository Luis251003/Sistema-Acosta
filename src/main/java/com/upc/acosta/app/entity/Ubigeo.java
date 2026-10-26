package com.upc.acosta.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_ubigeos")
@Getter
@Setter
@NoArgsConstructor
public class Ubigeo {

    @Id
    @Column(name = "cod_ubigeo", nullable = false, length = 6)
    private String codigo;

    @Column(nullable = false, length = 2)
    private String codDepartamento;

    @Column(nullable = false, length = 2)
    private String codProvincia;

    @Column(nullable = false, length = 2)
    private String codDistrito;

    @Column(nullable = false, length = 100)
    private String departamento;

    @Column(nullable = false, length = 100)
    private String provincia;

    @Column(nullable = false, length = 100)
    private String distrito;
}
