-- PROPOSITO: CREAR ENTIDAD LINEAS NAVIERAS
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_lineas_navieras(
    cod_linea_naviera CHAR(4) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY(cod_linea_naviera),
    CONSTRAINT uk_tb_lineas_navieras_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;