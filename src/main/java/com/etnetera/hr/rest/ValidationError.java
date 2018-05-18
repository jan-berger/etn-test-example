package com.etnetera.hr.rest;

/**
 * 
 * Validation error. Represents JSON response.
 * 
 * @author Etnetera
 *
 */
public class ValidationError extends SimpleError {

	private String field;
	
	public ValidationError(String field, String message) {
		super(message);
		this.field = field;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
		
}
