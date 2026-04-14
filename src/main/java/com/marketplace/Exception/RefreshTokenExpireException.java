package com.marketplace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RefreshTokenExpireException extends RuntimeException {
    public RefreshTokenExpireException(String message) {
        super(message);
    }
}
