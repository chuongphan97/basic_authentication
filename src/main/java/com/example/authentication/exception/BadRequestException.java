package com.example.authentication.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequestException extends Exception {
    private static final long serialVersionUID = 1L;

    private String error;
    private String message;

    private HttpStatus httpStatus;

    @JsonIgnore
    private boolean isPrintStackTrace;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String error, String message) {
        super(message);
        this.error = error;
        this.message = message;
    }

    public BadRequestException(String error, String message, HttpStatus httpStatus) {
        super(message);
        this.error = error;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
