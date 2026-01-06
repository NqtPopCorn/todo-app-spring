package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationException(ApplicationException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiErrorResponse.builder()
                        .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(400)
                .body(ApiErrorResponse.builder()
                        .message(e.getMessage())
                        .build());
    }

//    MissingServletRequestParameterException
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(400)
                .body(ApiErrorResponse.builder()
                        .message(e.getMessage())
                        .build());
}

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException e) {
//        return ResponseEntity.status(500)
//                .body(ApiErrorResponse.builder()
//                        .message("Internal Server Error")
//                        .build());
//    }
}
