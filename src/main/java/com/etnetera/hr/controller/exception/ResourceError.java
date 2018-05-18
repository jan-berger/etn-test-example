package com.etnetera.hr.controller.exception;

import java.util.function.Supplier;

/**
 * Error in REST API client call. Can supply or throw corresponding exception.
 *
 * @author Berger
 */
public enum ResourceError {
	NOT_FOUND,
	INCONSISTENT_IDS;

	public void throwException() throws ResourceException {
		throw new ResourceException(this);
	}

	public Supplier<ResourceException> supplyException() {
		return() -> new ResourceException(this);
	}
}
