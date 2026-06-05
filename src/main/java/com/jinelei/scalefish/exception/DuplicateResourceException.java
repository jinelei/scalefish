package com.jinelei.scalefish.exception;

public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String resourceName, String key) {
        super(ErrorCode.DUPLICATE_RESOURCE, resourceName + " already exists: " + key);
    }
}
