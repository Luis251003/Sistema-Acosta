-- PROPOSITO: CREAR ENTIDAD FLOTA
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_flotas(
    id_flota BIGINT NOT NULL AUTO_INCREMENT,
    rol_flota VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    id_operacion BIGINT NOT NULL,
    id_conductor BIGINT NOT NULL,
    id_camion BIGINT NOT NULL,

    PRIMARY KEY(id_flota),
    CONSTRAINT fk_tb_flotas_operacion
    FOREIGN KEY (id_operacion)
    REFERENCES tb_operaciones (id_operacion),
    CONSTRAINT fk_tb_flotas_conductor
    FOREIGN KEY (id_conductor)
    REFERENCES tb_conductores (id_conductor),
    CONSTRAINT fk_tb_flotas_camion
    FOREIGN KEY (id_camion)
    REFERENCES tb_camiones (id_camion)
)