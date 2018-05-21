package com.etnetera.hr.controller.exception;

import org.springframework.validation.BindingResult;

/**
 * 
 * Exception is thrown when input data are invalid.
 * 
 * @author Etnetera
 *
 */
public class InvalidDataException extends Throwable {
	
	private static final long serialVersionUID = -71568990486694572L;

	private final BindingResult result;
	
	public InvalidDataException(BindingResult result) {
		super();
		this.result = result;
	}
	
	public BindingResult getResult() {
		return result;
	}

}
