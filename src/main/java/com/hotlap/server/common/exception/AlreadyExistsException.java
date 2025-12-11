package com.hotlap.server.common.exception;

public class AlreadyExistsException extends ApplicationException {
    public AlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
