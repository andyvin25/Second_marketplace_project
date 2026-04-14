package com.marketplace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceDuplicationException extends RuntimeException {
    public ResourceDuplicationException(String message) {
        super(message);
    }
}
