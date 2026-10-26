package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Entity
@Table(name = "tb_documentos_camion")
@Getter
@Setter
public class DocumentoCamion extends Documento{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_camion", nullable = false)
    private Camion camion;

    public DocumentoCamion(
            String nombre,
            TipoDocumento tipoDocumento,
            String rutaArchivo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            Camion camion
    ) {
        super(nombre, tipoDocumento, rutaArchivo, fechaEmision, fechaVencimiento);
        this.camion = camion;
    }
}
