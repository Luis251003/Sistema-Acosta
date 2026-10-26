package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento", nullable = false)
    private Long id;

    @Column(name = "nombre_archivo", nullable = false, length = 100)
    private String nombre;

    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    public Documento(
            String nombre,
            TipoDocumento tipoDocumento,
            String rutaArchivo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento
    ) {
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.rutaArchivo = rutaArchivo;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        actualizarEstado();
    }

    @PrePersist
    private void configurarAntesDePersistir() {
        LocalDateTime ahora = LocalDateTime.now();
        this.createdAt = ahora;
        this.updatedAt = ahora;
        actualizarEstado();
    }

    @PreUpdate
    private void configurarAntesDeActualizar() {
        this.updatedAt = LocalDateTime.now();
        actualizarEstado();
    }

    public String obtenerEstadoActual() {
        LocalDate hoy = LocalDate.now();
        LocalDate umbralPorVencer = hoy.plusMonths(1);

        if (fechaVencimiento.isBefore(hoy)) {
            return "VENCIDO";
        }
        if (!fechaVencimiento.isAfter(umbralPorVencer)) {
            return "POR VENCER";
        }
        return "VIGENTE";
    }

    public void actualizarEstado() {
        this.estado = obtenerEstadoActual();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Documento that = (Documento) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
