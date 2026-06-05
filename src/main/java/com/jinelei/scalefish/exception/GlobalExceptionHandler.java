package com.jinelei.scalefish.exception;

import com.jinelei.scalefish.dto.GenericResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<GenericResult<Void>> handleBusinessException(BusinessException e) {
        log.warn("Business exception: {}", e.getMessage());
        ErrorCode ec = e.getErrorCode();
        GenericResult<Void> result = GenericResult.error(ec.getCode(), e.getMessage());
        return ResponseEntity.status(ec.getHttpStatus()).body(result);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericResult<Void>> handleBadCredentials(BadCredentialsException e) {
        log.warn("Auth failed: {}", e.getMessage());
        GenericResult<Void> result = GenericResult.error(401, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<GenericResult<Void>> handleNoSuchElement(NoSuchElementException e) {
        log.warn("Resource not found: {}", e.getMessage());
        GenericResult<Void> result = GenericResult.error(ErrorCode.NOT_FOUND.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResult<Void>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("validation error");
        log.warn("Validation error: {}", msg);
        GenericResult<Void> result = GenericResult.error(ErrorCode.VALIDATION_ERROR.getCode(), msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericResult<Void>> handleMalformedJson(HttpMessageNotReadableException e) {
        log.warn("Malformed request body: {}", e.getMessage());
        GenericResult<Void> result = GenericResult.error(ErrorCode.MALFORMED_REQUEST_BODY.getCode(), "malformed request body");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GenericResult<Void>> handleMissingParam(MissingServletRequestParameterException e) {
        log.warn("Missing parameter: {}", e.getMessage());
        GenericResult<Void> result = GenericResult.error(ErrorCode.MISSING_PARAMETER.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GenericResult<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("Method not supported: {}", e.getMessage());
        GenericResult<Void> result = GenericResult.error(ErrorCode.METHOD_NOT_ALLOWED.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResult<Void>> handleException(Exception e) {
        log.error("Unexpected error", e);
        GenericResult<Void> result = GenericResult.error(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
