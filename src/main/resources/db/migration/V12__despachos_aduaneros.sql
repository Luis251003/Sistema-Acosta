-- PROPOSITO: CREAR ENTIDAD DESPACHOS ADUANEROS
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_despachos_aduaneros (
    id_despacho_aduanero BIGINT NOT NULL AUTO_INCREMENT,
    canal VARCHAR(10) NOT NULL,
    modalidad VARCHAR(15) NULL,
    numero_contenedor VARCHAR(11) NULL,
    peso DECIMAL(10,2) NULL,
    hora_recojo_programado TIME(6) NULL,

    id_agencia_aduana BIGINT NOT NULL,
    id_operacion BIGINT NOT NULL,
    cod_linea_naviera CHAR(4) NULL,
    estado VARCHAR(15) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_despacho_aduanero),

    CONSTRAINT uk_tb_despachos_aduaneros_operacion UNIQUE (id_operacion),

    CONSTRAINT fk_tb_despachos_aduaneros_operacion
    FOREIGN KEY (id_operacion)
    REFERENCES tb_operaciones (id_operacion),

    CONSTRAINT fk_tb_despachos_aduaneros_linea_naviera
    FOREIGN KEY (cod_linea_naviera)
    REFERENCES tb_lineas_navieras (cod_linea_naviera),

    CONSTRAINT fk_tb_despacho_agencia_aduana
    FOREIGN KEY (id_agencia_aduana)
    REFERENCES tb_agencias_aduanas (id_agencia_aduana)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

UPDATE tb_despachos_aduaneros
SET estado = 'PENDIENTE',
    created_at = CURRENT_TIMESTAMP(6),
    updated_at = CURRENT_TIMESTAMP(6);


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

CREATE TABLE IF NOT EXISTS tb_despachos_extracciones (
    id_despacho_extraccion BIGINT NOT NULL AUTO_INCREMENT,
    campo VARCHAR(255) NOT NULL,
    valor_extraido VARCHAR(100),
    created_at DATETIME(6) NOT NULL,
    id_despacho_aduanero BIGINT,
    id_documento BIGINT,

    PRIMARY KEY (id_despacho_extraccion),
    CONSTRAINT fk_tb_despachos_extracciones_despacho
    FOREIGN KEY (id_despacho_aduanero)
    REFERENCES tb_despachos_aduaneros (id_despacho_aduanero),
    CONSTRAINT fk_tb_despachos_extracciones_documento
    FOREIGN KEY (id_documento)
    REFERENCES tb_documentos_despacho (id_documento)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;