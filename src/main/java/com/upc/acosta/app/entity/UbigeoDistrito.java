package com.upc.acosta.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_ubigeo_distritos")
@Getter
@Setter
@NoArgsConstructor
public class UbigeoDistrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubigeo_distrito", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String distrito;

    @Column(nullable = false, length = 6, unique = true)
    private String ubigeo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubigeo_provincia", nullable = false)
    private UbigeoProvincia ubigeoProvincia;
}
