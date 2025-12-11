package com.hotlap.server.common.exception;

public class NotFoundException extends ApplicationException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
