package com.etnetera.hr.controller;

import com.etnetera.hr.controller.exception.InvalidDataException;
import com.etnetera.hr.controller.exception.ResourceException;
import com.etnetera.hr.rest.Errors;
import com.etnetera.hr.rest.SimpleError;
import com.etnetera.hr.rest.ValidationError;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.PersistenceException;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
		if (errorList.isEmpty()) {
			errorList = result.getAllErrors().stream()
					.sorted(Comparator.comparing(ObjectError::getObjectName))
					.map(e -> new ValidationError(e.getObjectName(), e.getDefaultMessage()))
					.collect(Collectors.toList());
		}
		return ResponseEntity.badRequest().body(Errors.ofErrors(errorList));
	}

	@ExceptionHandler({ResourceException.class})
	public ResponseEntity<Errors> handleNotFoundException(ResourceException ex) {
		Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error in request", ex);
		return ResponseEntity.badRequest().body(Errors.ofError(ex.getError().name(), ex.getMessage()));
	}

	@ExceptionHandler({PersistenceException.class})
	public ResponseEntity<Errors> handlePersistenceException(PersistenceException ex) {
		return ResponseEntity.badRequest().body(Errors.ofError("PERSISTENCE_ERROR", ex.getMessage()));
	}


}
