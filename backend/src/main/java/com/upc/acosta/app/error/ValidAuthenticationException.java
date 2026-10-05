package com.upc.acosta.app.error;

import org.springframework.security.core.AuthenticationException;

public class ValidAuthenticationException extends AuthenticationException {
    public ValidAuthenticationException(String message) {
        super(message);
    }
}
