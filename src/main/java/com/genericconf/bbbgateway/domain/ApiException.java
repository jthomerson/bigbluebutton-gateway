package com.genericconf.bbbgateway.domain;

public class ApiException extends Exception {
	private static final long serialVersionUID = 1L;

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

}
