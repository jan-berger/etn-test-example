package com.etnetera.hr.rest.validation;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.function.Function;

public class JavaScriptFrameworkVersionsValidator implements ConstraintValidator<JavaScriptFrameworkVersions, JavaScriptFramework> {

	@Override
	public boolean isValid(JavaScriptFramework framework, ConstraintValidatorContext context) {
		return framework.getVersions().stream().anyMatch(v -> validateEach(framework, v));
	}

	private boolean validateEach(JavaScriptFramework framework, JavaScriptFrameworkVersion v) {
		return validateField(framework.getVersions(), v, JavaScriptFrameworkVersion::getVersionOrder)
				&& validateField(framework.getVersions(), v, JavaScriptFrameworkVersion::getVersion);
	}

	private boolean validateField(List<JavaScriptFrameworkVersion> vs, JavaScriptFrameworkVersion v, Function<JavaScriptFrameworkVersion, Object> fieldFunction) {
		return fieldFunction.apply(v) != null && vs.stream().noneMatch(vv -> !vv.equals(v) && fieldFunction.apply(v).equals(fieldFunction.apply(vv)));
	}

	@Override
	public void initialize(JavaScriptFrameworkVersions constraintAnnotation) {

	}
}
