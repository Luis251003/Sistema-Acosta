INSERT INTO tb_tipo_documento_camion (nombre)
SELECT 'Tarjeta Única de circulación'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento_camion
    WHERE nombre = 'Tarjeta Única de circulación'
);

INSERT INTO tb_tipo_documento_camion (nombre)
SELECT 'Seguro Vehicular'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento_camion
    WHERE nombre = 'Seguro Vehicular'
);

INSERT INTO tb_tipo_documento_camion (nombre)
SELECT 'Certificado de Inspección técnica'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento_camion
    WHERE nombre = 'Certificado de Inspección técnica'
);

INSERT INTO tb_tipo_documento_camion (nombre)
SELECT 'Tarjeta de identificación vehicular'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento_camion
    WHERE nombre = 'Tarjeta de identificación vehicular'
);
