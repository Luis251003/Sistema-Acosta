package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_secuencia_codigos")
@NoArgsConstructor
@Getter
@Setter
public class SecuenciaCodigo {

    @Id
    @Column(nullable = false)
    private Integer anio;
    @Column(nullable = false)
    private Integer ultimo = 0;

    public SecuenciaCodigo(Integer anio){
        this.anio = anio;
    }
    public Integer siguiente(){return ++ultimo;}

}
