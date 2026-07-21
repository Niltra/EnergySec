package com.energysec.domain.exception;

public class InvalidMetricException extends RuntimeException {
    public InvalidMetricException(String message) {
        super(message);
    }
}
