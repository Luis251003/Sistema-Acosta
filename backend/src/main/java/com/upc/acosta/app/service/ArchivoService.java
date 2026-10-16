package com.upc.acosta.app.service;

import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.ExternalServiceException;
import com.upc.acosta.app.error.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ArchivoService implements IArchivoService {

    private static final String FOTO_URL_PREFIX = "/uploads/conductores/";
    private static final String DOCUMENTO_URL_PREFIX = "/uploads/documentos/";
    private static final long MAX_FOTO_SIZE = 5 * 1024 * 1024;
    private static final long MAX_DOCUMENTO_SIZE = 10 * 1024 * 1024;

    private final Path directorioFotos;
    private final Path directorioDocumentos;

    public ArchivoService(@Value("${app.archivos.directorio:uploads}") String directorio) {
        Path directorioBase = Path.of(directorio).toAbsolutePath().normalize();
        this.directorioFotos = directorioBase.resolve("conductores");
        this.directorioDocumentos = directorioBase.resolve("documentos");
    }

    @Override
    public String subir(MultipartFile archivo) {
        validar(archivo, MAX_FOTO_SIZE, "La fotografía no puede superar los 5 MB");
        String tipoContenido = archivo.getContentType();
        if (tipoContenido == null) {
            throw new BadRequestException("La fotografía debe ser JPG o PNG");
        }
        String extension = switch (tipoContenido) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> throw new BadRequestException("La fotografía debe ser JPG o PNG");
        };
        return guardar(archivo, directorioFotos, FOTO_URL_PREFIX, extension);
    }

    @Override
    public void eliminar(String url) {
        eliminar(url, directorioFotos, FOTO_URL_PREFIX, "jpg|png", "la fotografía");
    }

    @Override
    public String subirDocumento(MultipartFile archivo) {
        validar(archivo, MAX_DOCUMENTO_SIZE, "El documento no puede superar los 10 MB");
        String tipoContenido = archivo.getContentType();
        if (tipoContenido == null) {
            throw new BadRequestException("El documento debe ser PDF, JPG o PNG");
        }
        String extension = switch (tipoContenido) {
            case "application/pdf" -> ".pdf";
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> throw new BadRequestException("El documento debe ser PDF, JPG o PNG");
        };
        return guardar(archivo, directorioDocumentos, DOCUMENTO_URL_PREFIX, extension);
    }

    @Override
    public byte[] leerDocumento(String url) {
        Path archivo = resolverDocumento(url);
        if (!archivo.getFileName().toString().endsWith(".pdf")) {
            throw new BadRequestException("Solo se pueden devolver documentos PDF");
        }
        try {
            return Files.readAllBytes(archivo);
        } catch (NoSuchFileException exception) {
            throw new NotFoundException("El archivo del documento no fue encontrado");
        } catch (IOException exception) {
            throw new ExternalServiceException("No se pudo leer el documento localmente");
        }
    }

    @Override
    public void eliminarDocumento(String url) {
        eliminar(url, directorioDocumentos, DOCUMENTO_URL_PREFIX, "pdf|jpg|png", "el documento");
    }

    private String guardar(MultipartFile archivo, Path directorio, String prefijo, String extension) {
        String nombreArchivo = UUID.randomUUID() + extension;
        Path destino = directorio.resolve(nombreArchivo);

        try {
            Files.createDirectories(directorio);
            try (InputStream contenido = archivo.getInputStream()) {
                Files.copy(contenido, destino);
            }
            return prefijo + nombreArchivo;
        } catch (IOException exception) {
            throw new ExternalServiceException("No se pudo guardar el archivo localmente");
        }
    }

    private void eliminar(String url, Path directorio, String prefijo, String extensiones, String tipoArchivo) {
        if (url == null || !url.startsWith(prefijo)) {
            throw new BadRequestException("La ruta de " + tipoArchivo + " no es válida");
        }

        Path archivo = resolverArchivo(url, directorio, prefijo, extensiones, tipoArchivo);

        try {
            Files.deleteIfExists(archivo);
        } catch (IOException exception) {
            throw new ExternalServiceException("No se pudo eliminar " + tipoArchivo + " localmente");
        }
    }

    private Path resolverDocumento(String url) {
        if (url == null || !url.startsWith(DOCUMENTO_URL_PREFIX)) {
            throw new BadRequestException("La ruta del documento no es válida");
        }
        return resolverArchivo(url, directorioDocumentos, DOCUMENTO_URL_PREFIX, "pdf|jpg|png", "el documento");
    }

    private Path resolverArchivo(
            String url,
            Path directorio,
            String prefijo,
            String extensiones,
            String tipoArchivo
    ) {
        String nombreArchivo = url.substring(prefijo.length());
        String patronNombre = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.(" + extensiones + ")";
        if (!nombreArchivo.matches(patronNombre)) {
            throw new BadRequestException("La ruta de " + tipoArchivo + " no es válida");
        }

        Path archivo = directorio.resolve(nombreArchivo).normalize();
        if (!archivo.startsWith(directorio)) {
            throw new BadRequestException("La ruta de " + tipoArchivo + " no es válida");
        }
        return archivo;
    }

    private void validar(MultipartFile archivo, long maxSize, String mensajeTamano) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BadRequestException("El archivo es obligatorio");
        }
        if (archivo.getSize() > maxSize) {
            throw new BadRequestException(mensajeTamano);
        }
    }
}
