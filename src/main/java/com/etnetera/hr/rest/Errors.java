package com.etnetera.hr.rest;

import java.util.Collections;
import java.util.List;

/**
 * 
 * Envelope for the validation errors. Represents JSON response.
 * 
 * @author Etnetera
 *
 */
public class Errors {

	private List<SimpleError> errors;

	public Errors(List<SimpleError> errors) {
		this.errors = errors;
	}

	public List<SimpleError> getErrors() {
		return errors;
	}

	public void setErrors(List<SimpleError> errors) {
		this.errors = errors;
	}

	public static Errors ofError(String message) {
		return new Errors(Collections.singletonList(new SimpleError(message)));
	}

	public static Errors ofError(String message, String detail) {
		return new Errors(Collections.singletonList(new SimpleError(message, detail)));
	}

	public static Errors ofErrors(List<SimpleError> errors) {
		return new Errors(errors);
	}
		
}
