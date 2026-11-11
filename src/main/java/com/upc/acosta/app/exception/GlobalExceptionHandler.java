package com.upc.acosta.app.exception;

import com.upc.acosta.app.error.BadRequestException;
import com.upc.acosta.app.error.DataExistException;
import com.upc.acosta.app.error.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Manejamos la excepcion de validacion binding
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> errores = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errores.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> IllegalStateException(IllegalStateException exc){
        Map<String,String> error = new HashMap<>();
        error.put("error","Error de ingreso ilegal de datos");
        error.put("message",exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgumentException(IllegalArgumentException exc){
        Map<String,String> error = new HashMap<>();
        error.put("error","Error de ingreso ilegal de datos");
        error.put("message",exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> BadRequestException(BadRequestException exc){
        Map<String,String> error = new HashMap<>();
        error.put("error","Error en el ingreso de datos");
        error.put("message",exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex
    ) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Error en el ingreso de datos",
                "message", "El archivo supera el tamaño máximo permitido"
        ));
    }

    //Manejamos los datos no encontrados
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> NotFoundException(NotFoundException exc){
        Map<String,String> error = new HashMap<>();
        error.put("error","Error de dato NO encontrado");
        error.put("message",exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //En caso el correo del usuario exista
    @ExceptionHandler(DataExistException.class)
    public ResponseEntity<?> EmailExistException(DataExistException exc){
        Map<String,String> error = new HashMap<>();
        error.put("error","Error de conflicto de datos");
        error.put("message",exc.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof InvalidFormatException invalidEx
                && invalidEx.getTargetType().isEnum()) {

            Class<?> enumClass = invalidEx.getTargetType();

            String valoresPermitidos = Arrays.stream(enumClass.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            String mensaje = "Valor inválido: '" + invalidEx.getValue()
                    + "'. Valores permitidos: " + valoresPermitidos;

            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", mensaje));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje",
                        "El JSON enviado es inválido o tiene un formato incorrecto."));
    }
}
