package com.etnetera.hr.controller.exception;

/**
 * Exception represents error in REST API client call.
 *
 * @author Berger
 */
public class ResourceException extends Throwable {

	private final ResourceError error;

	public ResourceException(ResourceError error) {
		super(error.name());
		this.error = error;
	}
}
