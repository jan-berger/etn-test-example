package com.etnetera.hr.controller;

import com.etnetera.hr.controller.exception.InvalidDataException;
import com.etnetera.hr.controller.exception.ResourceError;
import com.etnetera.hr.controller.exception.ResourceException;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.service.JavasScriptFrameworkService;
import com.etnetera.hr.rest.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple REST controller for accessing application logic.
 *  
 * @author Etnetera
 *
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {
	
	private final JavasScriptFrameworkService service;

	@Autowired
	public JavaScriptFrameworkController(JavasScriptFrameworkService service) {
		this.service = service;
	}

	@RequestMapping(value = "/frameworks", method = RequestMethod.GET)
	public ResponseEntity<Iterable<JavaScriptFramework>> getFrameworks() throws ResourceException {
		return ResponseEntity.ok(service.get());
	}

	@RequestMapping(value = "/frameworks", method = RequestMethod.PUT)
	public ResponseEntity<JavaScriptFramework> createFramework(
			@Valid @RequestBody JavaScriptFramework framework,
			BindingResult bindingResult
	) throws InvalidDataException, ResourceException {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException(bindingResult);
		}
		framework.prepareForRequest();
		return ResponseEntity.ok(service.add(framework));
	}

	@RequestMapping(value = "/frameworks/{frameworkId}", method = RequestMethod.GET)
	public ResponseEntity<JavaScriptFramework> getFramework(
			@PathVariable("frameworkId") Long frameworkId
	) throws ResourceException {
		return ResponseEntity.ok(service.get(frameworkId));
	}

	@RequestMapping(value = "/frameworks/{frameworkId}", method = RequestMethod.POST)
	public ResponseEntity<JavaScriptFramework> editFramework(
			@PathVariable("frameworkId") Long frameworkId,
			@Valid @RequestBody JavaScriptFramework framework,
			BindingResult bindingResult
	) throws ResourceException, InvalidDataException {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException(bindingResult);
		}
		return ResponseEntity.ok(service.edit(frameworkId, framework));
	}

	@RequestMapping(value = "/frameworks/{frameworkId}", method = RequestMethod.DELETE)
	public ResponseEntity<JavaScriptFramework> deleteFramework(
			@PathVariable("frameworkId") Long frameworkId
	) throws ResourceException {
		return ResponseEntity.ok(service.delete(frameworkId));
	}

	@RequestMapping(value = "/frameworks/search", method = RequestMethod.POST)
	public ResponseEntity<Iterable<JavaScriptFramework>> searchFrameworks(
			@Valid @RequestBody Search search,
			BindingResult bindingResult
	) throws InvalidDataException {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException(bindingResult);
		}
		return ResponseEntity.ok(service.search(search.getName(), search.getVersion(), search.getHypeLevel()));
	}
}
