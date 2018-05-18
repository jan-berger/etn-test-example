package com.etnetera.hr.rest;

/**
 * General error in JSON response.
 *
 * @author Berger
 */
public class SimpleError {

	private String message;

	public SimpleError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
