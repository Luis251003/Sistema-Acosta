package com.upc.acosta.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface IArchivoService {
    String subir(MultipartFile archivo);
    void eliminar(String url);
    String subirDocumento(MultipartFile archivo);
    void eliminarDocumento(String url);
}
