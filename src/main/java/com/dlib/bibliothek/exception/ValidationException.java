
package com.dlib.bibliothek.exception;

/**
 * 
 * The type Validation exception.
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1951868237391568702L;

	/**
	 * 
	 * Instantiates a new Validation exception.
	 * 
	 * @param message the message
	 */
	public ValidationException(final String message) {
		super(message);
	}
}
