package com.energysec.infrastructure.rest.controller;

import com.energysec.domain.exception.InvalidMetricException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidMetricException.class)
    public ResponseEntity<ProblemDetail> handleInvalidMetric(InvalidMetricException ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Métrica Energética Inválida");
        problem.setType(URI.create("https://energysec.io/docs/errors/invalid-metric"));
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validación fallida en el payload (Out of Memory Protection o Datos Inválidos)");
        problem.setTitle("Payload Request Inválido");
        problem.setProperty("validation_errors", validationErrors);
        problem.setType(URI.create("https://energysec.io/docs/errors/payload-validation"));
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, WebRequest request) {
        // Logging the actual error internally is critical, but we don't expose it to the user.
        System.err.println("Unexpected Critical Error: " + ex.getMessage());
        ex.printStackTrace();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno inesperado. Contacte con el soporte del SOC.");
        problem.setTitle("Error Interno del Servidor");
        problem.setType(URI.create("https://energysec.io/docs/errors/internal"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
