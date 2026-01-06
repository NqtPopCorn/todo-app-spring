package com.example.demo.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final int HttpStatus;
    private final String message;

    public ApplicationException(int status, String message) {
        super(message);
        this.HttpStatus = status;
        this.message = message;
    }


}
