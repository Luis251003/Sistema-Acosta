package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "tb_puntos_origen")
@Getter
@Setter
@NoArgsConstructor
public class PuntoOrigen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_punto_origen")
    private Long id;

    @Column(name = "nombre", nullable = false,unique = true, length = 50)
    private String nombre;

    public PuntoOrigen(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PuntoOrigen puntoOrigen = (PuntoOrigen) o;
        return Objects.equals(id, puntoOrigen.id) && Objects.equals(nombre, puntoOrigen.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }
}
