package com.stocksignal.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDatabaseException(DataAccessException e) {
        log.error("🚨 [DB_ERROR] Database connection or query timeout occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database Error Occurred");
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<String> handleApiException(RestClientException e) {
        log.error("🚨 [API_ERROR] External API gateway service failure or timeout", e);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("External API Gateway Error Occurred");
    }
}
