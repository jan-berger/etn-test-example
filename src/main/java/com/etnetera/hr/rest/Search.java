package com.etnetera.hr.rest;

import com.etnetera.hr.data.JavaScriptFrameworkHypeLevel;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * REST input entity representing search.
 *
 * @author Berger
 */
public class Search {

	@Size(max = 30)
	private String name;

	@Size(max = 30)
	private String version;

	private JavaScriptFrameworkHypeLevel hypeLevel;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public JavaScriptFrameworkHypeLevel getHypeLevel() {
		return hypeLevel;
	}

	public void setHypeLevel(JavaScriptFrameworkHypeLevel hypeLevel) {
		this.hypeLevel = hypeLevel;
	}
}
