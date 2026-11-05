-- PROPOSITO: ACTUALIZAR DESPACHOS ADUANEROS Y CREAR SUS EXTRACCIONES
-- FECHA: 03 DE NOVIEMBRE DE 2026

ALTER TABLE tb_despachos_aduaneros
    MODIFY COLUMN modalidad VARCHAR(15) NULL,
    MODIFY COLUMN numero_contenedor VARCHAR(11) NULL,
    MODIFY COLUMN peso DECIMAL(10,2) NULL,
    MODIFY COLUMN cod_linea_naviera CHAR(4) NULL,
    ADD COLUMN estado VARCHAR(15) NULL,
    ADD COLUMN created_at DATETIME(6) NULL,
    ADD COLUMN updated_at DATETIME(6) NULL;

UPDATE tb_despachos_aduaneros
SET estado = 'PENDIENTE',
    created_at = CURRENT_TIMESTAMP(6),
    updated_at = CURRENT_TIMESTAMP(6);

ALTER TABLE tb_despachos_aduaneros
    MODIFY COLUMN estado VARCHAR(15) NOT NULL,
    MODIFY COLUMN created_at DATETIME(6) NOT NULL,
    MODIFY COLUMN updated_at DATETIME(6) NOT NULL,
    ADD CONSTRAINT uk_tb_despachos_aduaneros_operacion UNIQUE (id_operacion);

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
