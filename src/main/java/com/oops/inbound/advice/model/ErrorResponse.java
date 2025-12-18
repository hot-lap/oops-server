package com.oops.inbound.advice.model;

public record ErrorResponse(
        String errorCode,
        String reason
) {
}
