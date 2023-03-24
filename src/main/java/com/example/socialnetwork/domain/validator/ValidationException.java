package com.example.socialnetwork.domain.validator;

public class ValidationException extends RuntimeException{
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }
}
