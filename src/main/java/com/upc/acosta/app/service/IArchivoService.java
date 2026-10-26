package com.upc.acosta.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface IArchivoService {
    String subir(MultipartFile archivo);
    void eliminar(String url);
    ArchivoContenido leerFoto(String url);
    String subirDocumento(MultipartFile archivo);
    byte[] leerDocumento(String url);
    void eliminarDocumento(String url);
}
