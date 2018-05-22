package com.etnetera.hr.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

/**
 * Entity class representing version of JavaScript framework with releationship 1:m.
 *
 * @author Berger
 */
@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"framework", "version"}),
		@UniqueConstraint(columnNames = {"framework", "versionOrder"})
})
public class JavaScriptFrameworkVersion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty
	@Size(max = 30)
	@Column(nullable = false, length = 30)
	private String version;

	@NotNull
	@DecimalMin(value = "0")
	@Column(nullable = false)
	private Integer versionOrder;

	@NotNull
	@Column(nullable = false)
	private JavaScriptFrameworkHypeLevel hypeLevel;

	@NotNull
	@Column(nullable = false)
	private Date deprecationDate;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "framework")
	private JavaScriptFramework framework;

	public JavaScriptFrameworkVersion(String version, Integer versionOrder, JavaScriptFrameworkHypeLevel hypeLevel, Date deprecationDate, JavaScriptFramework framework) {
		this.version = version;
		this.versionOrder = versionOrder;
		this.hypeLevel = hypeLevel;
		this.deprecationDate = deprecationDate;
		this.framework = framework;
	}

	public JavaScriptFrameworkVersion() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getVersionOrder() {
		return versionOrder;
	}

	public void setVersionOrder(Integer versionOrder) {
		this.versionOrder = versionOrder;
	}

	public JavaScriptFrameworkHypeLevel getHypeLevel() {
		return hypeLevel;
	}

	public void setHypeLevel(JavaScriptFrameworkHypeLevel hypeLevel) {
		this.hypeLevel = hypeLevel;
	}

	public JavaScriptFramework getFramework() {
		return framework;
	}

	public void setFramework(JavaScriptFramework framework) {
		this.framework = framework;
	}

	public Date getDeprecationDate() {
		return deprecationDate;
	}

	public void setDeprecationDate(Date deprecationDate) {
		this.deprecationDate = deprecationDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof JavaScriptFrameworkVersion))
			return false;
		JavaScriptFrameworkVersion that = (JavaScriptFrameworkVersion) o;
		return Objects.equals(version, that.version) &&
				Objects.equals(framework, that.framework);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, framework);
	}

	@Override
	public String toString() {
		return "JavScriptFrameworkVersion{" +
				"id=" + id +
				", version='" + version + '\'' +
				", versionOrder=" + versionOrder +
				", hypeLevel=" + hypeLevel +
				'}';
	}
}
