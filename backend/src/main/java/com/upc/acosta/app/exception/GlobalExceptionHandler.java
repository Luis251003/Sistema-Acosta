package com.upc.acosta.app.exception;

import com.upc.acosta.app.error.EmailExistException;
import com.upc.acosta.app.error.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Manejamos la excepcion de validacion binding
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> ValidException(MethodArgumentNotValidException ex){
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

    //Manejamos los datos no encontrados
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> NotFoundException(NotFoundException exc){
        Map<String,String> error = new HashMap<>();
        error.put("error","NotFoundException");
        error.put("message",exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //En caso el correo del usuario exista
    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<?> EmailExistException(EmailExistException exc){
        Map<String,String> error = new HashMap<>();
        error.put("error","EmailExistException");
        error.put("message",exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
