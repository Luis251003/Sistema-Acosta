package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity(name = "tb_despachos_aduaneros")
@Getter
@Setter
@NoArgsConstructor
public class DespachoAduanero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho_aduanero")
    private Long id;

    @Column(nullable = false,length = 10)
    private String canal;

    @Column(nullable = false,length = 15)
    private String modalidad;

    @Column(nullable = false,length = 15)
    private String numeroContenedor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal peso;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operacion", nullable = false)
    private Operacion operacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_linea_naviera", nullable = false)
    private LineaNaviera lineaNaviera;


    //METODO CONSTRUCTOR
    public DespachoAduanero(String canal, String modalidad, String numeroContenedor, BigDecimal peso, Operacion operacion, LineaNaviera lineaNaviera) {
        this.canal = canal;
        this.modalidad = modalidad;
        this.numeroContenedor = numeroContenedor;
        this.peso = peso;
        this.operacion = operacion;
        this.lineaNaviera = lineaNaviera;
    }

    //METODO PARA EVALUAR ESTADO DEL DESPACHO
    public Boolean evaluarEstadoDespacho(List<TipoDocumento> tiposObligatorios,
                                       List<Documento> documentosDespacho,
                                       LocalDate fechaConsulta){
        return tiposObligatorios.stream().allMatch(tipo ->
                documentosDespacho.stream().anyMatch(documento ->
                        documento.getTipoDocumento().getRol().equals(tipo.getRol())
                                && documento.getFechaVencimiento() != null
                                && !documento.getFechaVencimiento()
                                .isBefore(fechaConsulta)
                )
        );
    }

    //METODOS DE EQUALS Y HASHCODE

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DespachoAduanero that = (DespachoAduanero) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
