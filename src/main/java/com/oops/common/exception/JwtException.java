package com.oops.common.exception;

public class JwtException extends ApplicationException {

	public JwtException(ErrorCode errorCode) {
		super(errorCode);
	}

	public JwtException(String message) {
		super(ErrorCode.INVALID_ACCESS_TOKEN_ERROR, message);
	}

}
