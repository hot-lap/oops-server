package com.oops.common.exception;

public class OAuthException extends ApplicationException {

	public OAuthException(ErrorCode errorCode) {
		super(errorCode);
	}

	public OAuthException(String message) {
		super(ErrorCode.GOOGLE_OAUTH_FAILED_ERROR, message);
	}

}
