package com.upc.acosta.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_documentos_camion")
@Getter
@Setter
@NoArgsConstructor
public class DocumentoCamion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento_camion")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_camion", nullable = false)
    private Camion camion;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_documento_camion", nullable = false)
    private TipoDocumentoCamion tipoDocumentoCamion;

    @NotBlank
    @Size(max = 500)
    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @NotNull
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @NotNull
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public DocumentoCamion(
            Camion camion,
            String nombre,
            TipoDocumentoCamion tipoDocumentoCamion,
            String rutaArchivo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento
    ) {
        this.camion = camion;
        this.nombre = nombre;
        this.tipoDocumentoCamion = tipoDocumentoCamion;
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
        if (o == null || getClass() != o.getClass()) return false;
        DocumentoCamion that = (DocumentoCamion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
