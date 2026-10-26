package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Entity
@Table(name = "tb_documentos_conductor")
@Getter
@Setter
public class DocumentoConductor extends Documento{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_conductor", nullable = false)
    private Conductor conductor;

    public DocumentoConductor(
            String nombre,
            TipoDocumento tipoDocumento,
            String rutaArchivo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            Conductor conductor
    ) {
        super(nombre, tipoDocumento, rutaArchivo, fechaEmision, fechaVencimiento);
        this.conductor = conductor;
    }

}
