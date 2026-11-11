package com.upc.acosta.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_lineas_navieras")
public class LineaNaviera {
    @Id
    @Column(name = "cod_linea_naviera", nullable = false, length = 4)
    private String codigo;
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    public LineaNaviera(String nombre) {
        this.nombre = nombre;
    }
}
