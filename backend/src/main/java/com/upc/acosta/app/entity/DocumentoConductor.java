package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "tb_documentos_conductor")
@Getter
@Setter
@NoArgsConstructor
public class DocumentoConductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_conductor", nullable = false)
    private Conductor conductor;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_documento_conductor", nullable = false)
    private TipoDocumentoConductor tipoDocumentoConductor;

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

    public DocumentoConductor(
            Conductor conductor,
            String nombre,
            TipoDocumentoConductor tipoDocumentoConductor,
            String rutaArchivo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento
    ) {
        this.conductor = conductor;
        this.nombre = nombre;
        this.tipoDocumentoConductor = tipoDocumentoConductor;
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
        return fechaVencimiento.isBefore(LocalDate.now()) ? "VENCIDO" : "VIGENTE";
    }

    private void actualizarEstado() {
        this.estado = obtenerEstadoActual();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentoConductor that = (DocumentoConductor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
