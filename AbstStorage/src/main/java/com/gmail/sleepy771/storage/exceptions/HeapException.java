package com.gmail.sleepy771.storage.exceptions;

public class HeapException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2153932401671798198L;
	private final String message;
	private final Throwable throwable;
	
	public HeapException(String message) {
		this(null, message);
	}
	
	public HeapException(Throwable throwable) {
		this(throwable, "");
	}
	
	public HeapException(Throwable throwable, String message) {
		this.message = message;
		this.throwable = throwable;
	}

}
