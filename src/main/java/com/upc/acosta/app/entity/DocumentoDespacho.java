package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Entity
@Table(name = "tb_documentos_despacho")
@Getter
@Setter
public class DocumentoDespacho extends Documento{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_despacho_aduanero", nullable = false)
    private DespachoAduanero despachoAduanero;

    public DocumentoDespacho(
            String nombre,
            TipoDocumento tipoDocumento,
            String rutaArchivo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            DespachoAduanero despachoAduanero
    ) {
        super(nombre, tipoDocumento, rutaArchivo, fechaEmision, fechaVencimiento);
        this.despachoAduanero = despachoAduanero;
    }

}
