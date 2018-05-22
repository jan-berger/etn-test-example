package com.etnetera.hr.repository;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkHypeLevel;

import java.util.Set;

/**
 * This interface defines support for JavaScriptFramework search operation
 *
 * @author Berger
 */
public interface JavaScriptFrameworkRepositoryExtension {

	Iterable<JavaScriptFramework> search(String name, String version, JavaScriptFrameworkHypeLevel hypeLevel);
}
