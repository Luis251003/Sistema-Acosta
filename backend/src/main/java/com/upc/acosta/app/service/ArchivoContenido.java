package com.upc.acosta.app.service;

import org.springframework.http.MediaType;

public record ArchivoContenido(byte[] contenido, MediaType tipoContenido) {
}
