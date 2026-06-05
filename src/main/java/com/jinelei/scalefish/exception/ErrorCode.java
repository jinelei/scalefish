package com.jinelei.scalefish.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    SUCCESS(200, "success", HttpStatus.OK),
    CREATED(201, "created", HttpStatus.CREATED),
    NO_CONTENT(204, "no content", HttpStatus.NO_CONTENT),

    BAD_REQUEST(40000, "bad request", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(40001, "validation error", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER(40002, "missing parameter", HttpStatus.BAD_REQUEST),
    MALFORMED_REQUEST_BODY(40003, "malformed request body", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED(40004, "method not allowed", HttpStatus.METHOD_NOT_ALLOWED),

    NOT_FOUND(40400, "resource not found", HttpStatus.NOT_FOUND),

    CONFLICT(40900, "resource conflict", HttpStatus.CONFLICT),
    DUPLICATE_RESOURCE(40901, "resource already exists", HttpStatus.CONFLICT),

    INTERNAL_ERROR(50000, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
