package com.upc.acosta.app.error;

public class DataExistException extends RuntimeException{
    public DataExistException(String message) {
        super(message);
    }
}
