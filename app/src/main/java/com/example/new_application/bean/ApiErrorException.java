package com.example.new_application.bean;

public class ApiErrorException extends Exception {
    private final ErrorEnvelope errorEnvelope;

    public ApiErrorException(ErrorEnvelope errorEnvelope) {
        super(errorEnvelope.message);

        this.errorEnvelope = errorEnvelope;
    }
}
