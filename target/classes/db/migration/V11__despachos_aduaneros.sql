-- PROPOSITO: CREAR ENTIDAD DESPACHOS ADUANEROS
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_despachos_aduaneros (
    id_despacho_aduanero BIGINT NOT NULL AUTO_INCREMENT,
    canal VARCHAR(10) NOT NULL,
    modalidad VARCHAR(15) NOT NULL,
    numero_contenedor VARCHAR(12) NOT NULL,
    peso DECIMAL(10,2) NOT NULL,
    id_operacion BIGINT NOT NULL,
    cod_linea_naviera CHAR(4) NOT NULL,
    PRIMARY KEY (id_despacho_aduanero),

    CONSTRAINT fk_tb_despachos_aduaneros_operacion
    FOREIGN KEY (id_operacion)
    REFERENCES tb_operaciones (id_operacion),

    CONSTRAINT fk_tb_despachos_aduaneros_linea_naviera
    FOREIGN KEY (cod_linea_naviera)
    REFERENCES tb_lineas_navieras (cod_linea_naviera)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_documentos_despacho (
    id_documento BIGINT NOT NULL AUTO_INCREMENT,
    nombre_archivo VARCHAR(100) NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    id_tipo_documento BIGINT NOT NULL,
    id_despacho_aduanero BIGINT NOT NULL,

    PRIMARY KEY (id_documento),
    CONSTRAINT fk_tb_documentos_despacho
    FOREIGN KEY (id_despacho_aduanero)
    REFERENCES tb_despachos_aduaneros (id_despacho_aduanero),
    CONSTRAINT fk_tb_documentos_despacho_tipo
    FOREIGN KEY (id_tipo_documento)
    REFERENCES tb_tipo_documento (id_tipo_documento)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
