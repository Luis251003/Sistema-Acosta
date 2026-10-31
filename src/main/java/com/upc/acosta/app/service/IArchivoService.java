package com.upc.acosta.app.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface IArchivoService {
    URL obtenerUrlTemporal(String key);
    String subirFoto(MultipartFile archivo);
    String subirDocumento(MultipartFile archivo,String folder);
    void eliminarArchivo(String key);
}
