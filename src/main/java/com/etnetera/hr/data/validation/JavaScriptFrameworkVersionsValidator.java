package com.etnetera.hr.data.validation;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.function.Function;

/**
 * Actual validator for JavaScriptFrameworkVersions validator
 *
 * @author Berger
 */
public class JavaScriptFrameworkVersionsValidator implements ConstraintValidator<JavaScriptFrameworkVersions, JavaScriptFramework> {

	@Override
	public boolean isValid(JavaScriptFramework framework, ConstraintValidatorContext context) {
		return framework.getVersions().isEmpty() || framework.getVersions().stream().allMatch(v -> validateEach(framework, v));
	}

	private boolean validateEach(JavaScriptFramework framework, JavaScriptFrameworkVersion v) {
		return validateField(framework.getVersions(), v, JavaScriptFrameworkVersion::getVersionOrder)
				&& validateField(framework.getVersions(), v, JavaScriptFrameworkVersion::getVersion);
	}

	private boolean validateField(List<JavaScriptFrameworkVersion> vs, JavaScriptFrameworkVersion v, Function<JavaScriptFrameworkVersion, Object> fieldFunction) {
		return fieldFunction.apply(v) != null && vs.stream().filter(vv -> fieldFunction.apply(v).equals(fieldFunction.apply(vv))).count() <= 1;
	}

	@Override
	public void initialize(JavaScriptFrameworkVersions constraintAnnotation) {

	}
}
