package com.etnetera.hr.repository;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkHypeLevel;

import java.util.Set;

public interface JavaScriptFrameworkRepositoryExtension {

	Iterable<JavaScriptFramework> search(String name, String version, JavaScriptFrameworkHypeLevel hypeLevel);
}
