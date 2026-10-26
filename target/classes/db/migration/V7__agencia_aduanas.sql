-- PROPOSITO: CREAR ENTIDAD AGENCIA ADUANAS
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_agencias_aduanas (
    id_agencia_aduana BIGINT NOT NULL AUTO_INCREMENT,
    ruc CHAR(11) NOT NULL,
    razon_social VARCHAR(200) NOT NULL,
    id_estado BIGINT NOT NULL,
    id_condicion BIGINT NOT NULL,
    PRIMARY KEY (id_agencia_aduana),
    CONSTRAINT uk_tb_agencia_ruc UNIQUE (ruc),
    CONSTRAINT fk_tb_agencias_aduanas_estado
    FOREIGN KEY (id_estado)
    REFERENCES tb_estado_contribuyente (id_estado_contribuyente),
    CONSTRAINT fk_tb_agencias_aduanas_condicion
    FOREIGN KEY (id_condicion)
    REFERENCES tb_condicion_contribuyente (id_condicion_contribuyente)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

