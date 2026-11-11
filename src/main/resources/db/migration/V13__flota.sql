-- PROPOSITO: CREAR ENTIDAD FLOTA
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_flotas(
    id_flota BIGINT NOT NULL AUTO_INCREMENT,
    id_operacion BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_flota),
    CONSTRAINT uk_tb_flotas_operacion UNIQUE (id_operacion),
    CONSTRAINT fk_tb_flotas_nueva_operacion
    FOREIGN KEY (id_operacion) REFERENCES tb_operaciones (id_operacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tb_flotas_detalle (
   id_flota_detalle BIGINT NOT NULL AUTO_INCREMENT,
   id_flota BIGINT NOT NULL,
   id_conductor BIGINT NOT NULL,
   id_camion BIGINT NOT NULL,

   rol_flota VARCHAR(20) NOT NULL,
   created_at DATETIME(6) NOT NULL,
   updated_at DATETIME(6) NOT NULL,

   PRIMARY KEY (id_flota_detalle),

   CONSTRAINT fk_tb_flotas_detalle_flota
   FOREIGN KEY (id_flota) REFERENCES tb_flotas (id_flota),

   CONSTRAINT uk_tb_flotas_detalle_flota_conductor
   UNIQUE (id_flota, id_conductor),

   CONSTRAINT uk_tb_flotas_detalle_flota_rol
   UNIQUE (id_flota, rol_flota),

   CONSTRAINT fk_tb_flotas_detalle_conductor
   FOREIGN KEY (id_conductor) REFERENCES tb_conductores (id_conductor),

   CONSTRAINT fk_tb_flotas_detalle_camion
   FOREIGN KEY (id_camion) REFERENCES tb_camiones (id_camion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;