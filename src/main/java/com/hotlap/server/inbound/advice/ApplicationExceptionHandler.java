package com.hotlap.server.inbound.advice;

import com.hotlap.server.common.exception.ApplicationException;
import com.hotlap.server.inbound.advice.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleApplicationException(ApplicationException e) {
        log.error("[ERROR] BusinessException -> {}, {}", e.getMessage(), e.getCause());
        return new ErrorResponse(e.getErrorCode().name(), e.getMessage());
    }
}
