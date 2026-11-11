package com.upc.acosta.app.service;

import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.service.interfaces.IArchivoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
public class ArchivoService implements IArchivoService {

    private final S3Client s3Client;
    private final String bucket;
    private final S3Presigner s3Presigner;

    private static final long MAX_FOTO_SIZE = 5 * 1024 * 1024;
    private static final long MAX_DOCUMENTO_SIZE = 10 * 1024 * 1024;

    public ArchivoService(S3Client s3Client,
                          @Value("${R2_BUCKET}") String bucket,S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.s3Presigner = s3Presigner;
    }

    @Override
    public URL obtenerUrlTemporal(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(request)
                        .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    @Override
    public String subirFoto(MultipartFile archivo){

        //VALIDAR PROPIEDADES DEL DOCUMENTO
        validar(archivo, MAX_FOTO_SIZE, "La fotografía no puede superar los 5 MB");
        //VALIDAR TIPO DE ARCHIVO
        String tipoContenido = archivo.getContentType();
        if (!Objects.equals(tipoContenido, "image/jpeg") && !Objects.equals(tipoContenido, "image/png")) {
            throw new BadRequestException("La fotografía debe ser JPG o PNG");
        }
        //OBTENER METADATOS
        String extension = obtenerExtension(tipoContenido);
        String key = "fotos/" + UUID.randomUUID() + extension;

        //CONSTRUIMOS EL REQUEST
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(tipoContenido)
                .build();

        //SUBIMOS LA FOTO
        try{
            byte[] contenido = archivo.getBytes();
            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(contenido)
            );
        }catch (Exception e) {
            throw new BadRequestException("Error al subir la fotografía");
        }

        // GUARDAMOS LA CLAVE EN LA DB
        return key;
    }

    @Override
    public String subirDocumento(MultipartFile archivo, String folder){

        //VALIDAR TAMANIO DE DOCUMENTO
        validar(archivo, MAX_DOCUMENTO_SIZE, "El documento no puede superar los 10 MB");

        //VALIDAR TIPO DE CONTENIDO
        String tipoContenido = archivo.getContentType();
        if (!Objects.equals(tipoContenido, "application/pdf")) {
            throw new BadRequestException("El archivo debe ser PDF");
        }

        //OBTENER EXTENSION
        String extension = obtenerExtension(tipoContenido);

        //GENERAR LA KEY PARA EL DOCUMENTO EN S3
        String key = "documentos/"+ folder + "/" + UUID.randomUUID() + extension;

        //CONVERTIMOS EL ARCHIVO A BYTES


        //CONSTRUIMOS EL REQUEST
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(tipoContenido)
                .build();

        //SUBIMOS EL ARCHIVO
        try{
            byte[] contenido = archivo.getBytes();
            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(contenido)
            );
        }catch (Exception e) {
            throw new BadRequestException("Error al subir el documento");
        }

        // GUARDAMOS LA KEY EN LA DB
        return key;
    }

    @Override
    public void eliminarArchivo(String key) {
        if (key == null || key.isBlank()) {
            return;
        }

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }

    private String obtenerExtension(String contentType) {
        return switch (contentType) {
            case "application/pdf" -> ".pdf";
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> throw new BadRequestException("Tipo de archivo no soportado");
        };
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
