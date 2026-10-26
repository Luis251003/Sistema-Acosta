-- PROPOSITO: INSERTAR TIPOS DE DOCUMENTOS CAMION
-- FECHA: 07 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

INSERT INTO tb_tipo_documento (nombre, rol)
SELECT 'Tarjeta Unica de Circulacion', 'CAMION'
    WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento
    WHERE nombre = 'Tarjeta Unica de Circulacion'
);

INSERT INTO tb_tipo_documento (nombre, rol)
SELECT 'Seguro Vehicular', 'CAMION'
    WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento
    WHERE nombre = 'Seguro Vehicular'
);

INSERT INTO tb_tipo_documento (nombre, rol)
SELECT 'Certificado de Inspeccion Tecnica', 'CAMION'
    WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento
    WHERE nombre = 'Certificado de Inspeccion Tecnica'
);

INSERT INTO tb_tipo_documento (nombre, rol)
SELECT 'Tarjeta de Inspección Vehicular', 'CAMION'
    WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento
    WHERE nombre = 'Tarjeta de Inspección Vehicular'
);
