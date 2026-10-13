package com.upc.acosta.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String marca;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String modelo;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 8, fraction = 2)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal capacidad;

    @NotBlank
    @Size(max = 30)
    @Column(name = "numero_tuce", nullable = false, unique = true, length = 30)
    private String numeroTuce;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false, length = 20)
    private TipoVehiculo tipoVehiculo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCamion estado = EstadoCamion.INHABILITADO;

    @NotNull
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
