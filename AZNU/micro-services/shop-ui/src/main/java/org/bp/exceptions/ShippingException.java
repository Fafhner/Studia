package org.bp.exceptions;

public class ShippingException extends Exception {

	public ShippingException() {
	}

	public ShippingException(String message) {
		super(message);
	}

	public ShippingException(Throwable cause) {
		super(cause);
	}

	public ShippingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShippingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
