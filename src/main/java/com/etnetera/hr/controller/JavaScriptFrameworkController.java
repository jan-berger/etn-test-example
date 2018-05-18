package com.etnetera.hr.controller;

import com.etnetera.hr.controller.exception.InvalidDataException;
import com.etnetera.hr.controller.exception.ResourceError;
import com.etnetera.hr.controller.exception.ResourceException;
import com.etnetera.hr.rest.Search;
import com.sun.deploy.panel.ITreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

/**
 * Simple REST controller for accessing application logic.
 *  
 * @author Etnetera
 *
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {
	
	private final JavaScriptFrameworkRepository repository;

	@Autowired
	public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(value = "/frameworks", method = RequestMethod.GET)
	public ResponseEntity<Iterable<JavaScriptFramework>> getFrameworks() {
		return ResponseEntity.ok(repository.findAll());
	}

	@RequestMapping(value = "/frameworks", method = RequestMethod.PUT)
	public ResponseEntity<JavaScriptFramework> createFramework(
			@Valid @RequestBody JavaScriptFramework framework,
			BindingResult bindingResult
	) throws InvalidDataException {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException(bindingResult);
		}
		return ResponseEntity.ok(repository.save(framework));
	}

	@RequestMapping(value = "/frameworks/{frameworkId}", method = RequestMethod.GET)
	public ResponseEntity<JavaScriptFramework> getFramework(
			@PathParam("frameworkId") Long frameworkId
	) throws ResourceException {
		return ResponseEntity.ok(repository.findById(frameworkId)
				.orElseThrow(ResourceError.NOT_FOUND.supplyException()));
	}

	@RequestMapping(value = "/frameworks/{frameworkId}", method = RequestMethod.POST)
	public ResponseEntity<JavaScriptFramework> editFramework(
			@PathParam("frameworkId") Long frameworkId,
			@Valid @RequestBody JavaScriptFramework framework,
			BindingResult bindingResult
	) throws ResourceException, InvalidDataException {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException(bindingResult);
		}
		repository.findById(frameworkId)
				.orElseThrow(ResourceError.NOT_FOUND.supplyException());
		if (framework.getId() != null && !framework.getId().equals(frameworkId)) {
			ResourceError.INCONSISTENT_IDS.throwException();
		}
		return ResponseEntity.ok(repository.save(framework));
	}

	@RequestMapping(value = "/frameworks/{frameworkId}", method = RequestMethod.DELETE)
	public ResponseEntity<JavaScriptFramework> deleteFramework(
			@PathParam("frameworkId") Long frameworkId
	) throws ResourceException {
		JavaScriptFramework framework = repository.findById(frameworkId)
				.orElseThrow(ResourceError.NOT_FOUND.supplyException());
		repository.delete(framework);
		return ResponseEntity.ok(framework);
	}

	@RequestMapping(value = "/frameworks/search", method = RequestMethod.POST)
	public ResponseEntity<Iterable<JavaScriptFramework>> searchFrameworks(
			@Valid @RequestBody Search search,
			BindingResult bindingResult
	) throws InvalidDataException {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException(bindingResult);
		}
		return ResponseEntity.ok(repository.search(search.getName(), search.getVersion(), search.getHypeLevel()));
	}
}
