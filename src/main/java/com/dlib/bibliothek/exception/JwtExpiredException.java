
package com.dlib.bibliothek.exception;

/**
 * 
 * The type JwtExpiredException exception.
 */
public class JwtExpiredException extends RuntimeException {

	private static final long serialVersionUID = 1951868237391568702L;

	/**
	 * 
	 * Instantiates a new JwtExpiredException exception.
	 * 
	 * @param message the message
	 */
	public JwtExpiredException(final String message) {
		super(message);
	}
}
