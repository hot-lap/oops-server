package com.hotlap.server.common.exception;

public class InvalidRequestException extends ApplicationException {
    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
