package com.jinelei.scalefish.exception;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceName, Object id) {
        super(ErrorCode.NOT_FOUND, resourceName + " not found: " + id);
    }
}
