package com.etnetera.hr.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.etnetera.hr.controller.exception.ResourceException;
import com.etnetera.hr.rest.SimpleError;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.etnetera.hr.controller.exception.InvalidDataException;
import com.etnetera.hr.rest.Errors;
import com.etnetera.hr.rest.ValidationError;

/**
 * 
 * Main REST controller.
 * 
 * @author Etnetera
 *
 */
public abstract class EtnRestController {

	@ExceptionHandler({InvalidDataException.class})
	public ResponseEntity<Errors> handleValidationException(InvalidDataException ex) {
		BindingResult result = ex.getResult();
		List<SimpleError> errorList = result.getFieldErrors().stream()
				.sorted(Comparator.comparing(FieldError::getField))
				.map(e -> new ValidationError(e.getField(), e.getCode()))
				.collect(Collectors.toList());
		return ResponseEntity.badRequest().body(Errors.ofErrors(errorList));
	}

	@ExceptionHandler({ResourceException.class})
	public ResponseEntity<Errors> handleNotFoundException(ResourceException ex) {
		return ResponseEntity.badRequest().body(Errors.ofError(ex.getMessage()));
	}
	
}
