package com.oops.common.exception;

public class EncryptionException extends ApplicationException {

	public EncryptionException(ErrorCode errorCode) {
		super(errorCode);
	}

	public EncryptionException(String message) {
		super(ErrorCode.ENCRYPTION_ERROR, message);
	}

}
