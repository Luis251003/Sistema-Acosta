package com.upc.acosta.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoService implements IArchivoService{
    @Override
    public String subir(MultipartFile archivo) {
        return "";
    }

    @Override
    public void eliminar(String url) {

    }
}
