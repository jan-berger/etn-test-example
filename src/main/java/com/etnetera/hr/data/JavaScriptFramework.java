package com.etnetera.hr.data;

import com.etnetera.hr.data.validation.JavaScriptFrameworkVersions;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

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

	/**
	 * Adds version and sets it's framework
	 *
	 * @param version to be added
	 */
	public void addVersion(JavaScriptFrameworkVersion version) {
		version.setFramework(this);
		getVersions().add(version);
	}

	/**
	 * Merges new versions with old versions based on ID
	 *
	 * @param versions to be merged with existing versions
	 */
	public void addVersions(List<JavaScriptFrameworkVersion> versions) {
		versions.forEach(newVersion -> {
			Optional<JavaScriptFrameworkVersion> version = getVersions().stream()
					.filter(v -> v.getId() != null && v.getId().equals(newVersion.getId())).findAny();
			if (version.isPresent()) {
				if (!version.get().equals(newVersion)) {
					version.get().setFramework(this);
					version.get().setVersion(newVersion.getVersion());
					version.get().setHypeLevel(newVersion.getHypeLevel());
					version.get().setDeprecationDate(newVersion.getDeprecationDate());
				}
			} else {
				addVersion(newVersion);
			}
		});
	}

	/**
	 * Adds version with corresponding order and framework
	 *
	 * @param version of new version
	 * @param hypeLevel of new version
	 * @param deprecationDate of new version
	 */
	public void addVersion(String version, JavaScriptFrameworkHypeLevel hypeLevel, Date deprecationDate) {
		Integer versionOrder = getCurrentVersion().map(JavaScriptFrameworkVersion::getVersionOrder).orElse(0) + 10;
		addVersion(new JavaScriptFrameworkVersion(version, versionOrder, hypeLevel, deprecationDate, this));
	}

	/**
	 * Prepares object for request
	 */
	public void prepareForRequest() {
		getVersions().forEach(v -> v.setFramework(this));
	}

	/**
	 * Deletes versions from it's collection and sets framework to null
	 *
	 * @param versionsToRemove
	 */
	public void clearVersions(List<JavaScriptFrameworkVersion> versionsToRemove) {
		versionsToRemove.forEach(v -> {
			v.setFramework(null);
			getVersions().remove(v);
		});
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
