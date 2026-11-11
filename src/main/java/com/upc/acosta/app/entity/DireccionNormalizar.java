package com.upc.acosta.app.entity;

import java.text.Normalizer;

public final class DireccionNormalizar {

    private DireccionNormalizar(){}

    public static String normalizar(String direccion){
        if(direccion == null) return "";
        String s = Normalizer.normalize(direccion.toLowerCase(),Normalizer.Form.NFD).replace("\\p{M}", "");
        s = s.replaceAll("\\(.*?\\)", " ");                       // quita lo que va entre paréntesis
        s = s.replaceAll("^\\s*\\d+\\s*-\\s*", "");               // quita código inicial "3014 -"
        s = s.replaceAll("prov\\.? const\\.? del callao", " ");   // quita provincia completa
        s = s.replaceAll("\\bprov\\b\\.?", " ");                  // quita "prov" suelto
        s = s.replaceAll("\\b(nro|nº|n°)\\.?", " ");              // quita "nro"
        s = s.replaceAll("[^a-z0-9 ]", " ");                      // quita símbolos y guiones
        return s.replaceAll("\\s+", " ").trim();
    }

}
