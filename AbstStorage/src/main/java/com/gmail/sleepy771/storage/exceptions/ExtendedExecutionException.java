package com.gmail.sleepy771.storage.exceptions;

import java.util.concurrent.ExecutionException;

public class ExtendedExecutionException extends ExecutionException {

	/**
     * 
     */
	private static final long serialVersionUID = 757102614093288687L;

	private final String objectName;

	public ExtendedExecutionException(ExecutionException e, String o) {
		super(e);
		this.objectName = o;
	}

	public final String conflictingObject() {
		return this.objectName;
	}

}
