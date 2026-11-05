package com.upc.acosta.app.entity;

import com.upc.acosta.app.entity.tipos.EstadoCamion;
import com.upc.acosta.app.entity.tipos.TipoVehiculo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_camiones")
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camion")
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal capacidad;

    @Column(name = "numero_tuce", nullable = false, unique = true, length = 30)
    private String numeroTuce;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false, length = 20)
    private TipoVehiculo tipoVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCamion estado = EstadoCamion.INHABILITADO;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Camion(String placa, String marca, String modelo, BigDecimal capacidad,
                  String numeroTuce, TipoVehiculo tipoVehiculo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.numeroTuce = numeroTuce;
        this.tipoVehiculo = tipoVehiculo;
    }

    @PrePersist
    private void configurar() {
        LocalDateTime ahora = LocalDateTime.now();
        this.createdAt = ahora;
        this.updatedAt = ahora;
    }

    @PreUpdate
    private void configurarAntesDeActualizar() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Camion camion = (Camion) o;
        return Objects.equals(id, camion.id) && Objects.equals(placa, camion.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placa);
    }
}
