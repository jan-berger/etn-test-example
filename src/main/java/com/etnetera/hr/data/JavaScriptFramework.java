package com.etnetera.hr.data;

import com.etnetera.hr.rest.validation.JavaScriptFrameworkVersions;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 * 
 * @author Etnetera
 *
 */
@Entity
@JavaScriptFrameworkVersions
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@NotEmpty
	@Size(max = 30)
	@Column(nullable = false, length = 30, unique = true)
	private String name;

	@Valid
	@OneToMany(mappedBy = "framework", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<JavaScriptFrameworkVersion> versions = new ArrayList<>();

	
	public JavaScriptFramework() {
	}

	public JavaScriptFramework(String name) {
		this.name = name;
	}

	public Optional<JavaScriptFrameworkHypeLevel> getHypeLevel() {
		return getCurrentVersion().map(JavaScriptFrameworkVersion::getHypeLevel);
	}

	public Optional<Date> getDeprecationDate() {
		return getCurrentVersion().map(JavaScriptFrameworkVersion::getDeprecationDate);
	}

	public Optional<JavaScriptFrameworkVersion> getCurrentVersion() {
		return versions.stream().max(Comparator.comparing(JavaScriptFrameworkVersion::getVersionOrder));
	}

	public void addVersion(JavaScriptFrameworkVersion version) {
		version.setFramework(this);
		getVersions().add(version);
	}

	public void addVersion(String version, JavaScriptFrameworkHypeLevel hypeLevel, Date deprecationDate) {
		Integer versionOrder = getCurrentVersion().map(JavaScriptFrameworkVersion::getVersionOrder).orElse(0) + 10;
		addVersion(new JavaScriptFrameworkVersion(version, versionOrder, hypeLevel, deprecationDate, this));
	}

	public void prepareForRequest() {
		getVersions().forEach(v -> v.setFramework(this));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<JavaScriptFrameworkVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<JavaScriptFrameworkVersion> versions) {
		this.versions = versions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof JavaScriptFramework))
			return false;
		JavaScriptFramework that = (JavaScriptFramework) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {

		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "JavaScriptFramework{" +
				"id=" + id +
				", name='" + name + '\'' +
				", versions=" + versions +
				'}';
	}
}
