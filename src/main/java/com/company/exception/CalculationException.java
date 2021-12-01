package com.company.exception;

public class CalculationException extends Exception{
	public CalculationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CalculationException(String message) {
		super(message);
	}

	public CalculationException(Throwable cause) {
		super(cause);
	}

	public CalculationException() {
		super();
	}
}
