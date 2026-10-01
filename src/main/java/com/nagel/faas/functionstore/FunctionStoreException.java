package com.nagel.faas.functionstore;

public class FunctionStoreException extends RuntimeException {

	public FunctionStoreException(String message) {
		super(message);
	}

	public FunctionStoreException(String message, Throwable cause) {
		super(message, cause);
	}
}
