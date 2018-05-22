package com.etnetera.hr.service;

import com.etnetera.hr.controller.exception.ResourceException;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepositoryExtension;

/**
 * Service handling all requests for JavaScriptFramework
 *
 * @author Berger
 */
public interface JavasScriptFrameworkService extends JavaScriptFrameworkRepositoryExtension {

	JavaScriptFramework edit(Long frameworkId, JavaScriptFramework newFramework) throws ResourceException;
	JavaScriptFramework add(JavaScriptFramework newFramework) throws ResourceException;
	Iterable<JavaScriptFramework> get() throws ResourceException;
	JavaScriptFramework get(Long frameworkId) throws ResourceException;
	JavaScriptFramework delete(Long frameworkId) throws ResourceException;
}
