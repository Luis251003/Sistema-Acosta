-- CREAMOS LOS PUNTOS DE ORIGEN
CREATE TABLE IF NOT EXISTS tb_puntos_origen(
    id_punto_origen BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY(id_punto_origen),

    CONSTRAINT uk_tb_puntos_origen_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- CREAMOS LAS ALIAS PARA PUNTO DE ORIGEN
CREATE TABLE IF NOT EXISTS tb_puntos_origen_alias(
    id_punto_origen_alias BIGINT NOT NULL AUTO_INCREMENT,
    id_punto_origen BIGINT NOT NULL,
    direccion_normalizada VARCHAR(300) NOT NULL,
    PRIMARY KEY(id_punto_origen_alias),
    CONSTRAINT fk_tb_puntos_origen_alias_punto_origen
    FOREIGN KEY (id_punto_origen)
    REFERENCES tb_puntos_origen (id_punto_origen)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;