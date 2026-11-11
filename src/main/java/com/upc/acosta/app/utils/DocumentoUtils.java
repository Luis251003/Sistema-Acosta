package com.upc.acosta.app.utils;

import com.upc.acosta.app.entity.Documento;
import com.upc.acosta.app.entity.TipoDocumento;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class DocumentoUtils {
    public static boolean isHabilitado(Set<TipoDocumento> tipoDocumento, List<? extends Documento> documentos) {
        LocalDate fechaConsulta = LocalDate.now();

        return !tipoDocumento.isEmpty() && tipoDocumento.stream().allMatch(tipo ->
                documentos.stream().anyMatch(documento ->
                        documento.getTipoDocumento().getIdTipoDocumento()
                                .equals(tipo.getIdTipoDocumento())
                                && documento.getFechaVencimiento() != null
                                && !documento.getFechaVencimiento().isBefore(fechaConsulta)
                )
        );
    }
}
