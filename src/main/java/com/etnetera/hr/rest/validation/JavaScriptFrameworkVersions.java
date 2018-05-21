package com.etnetera.hr.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = JavaScriptFrameworkVersionsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaScriptFrameworkVersions {
	String message() default "ERROR_JAVASCRIPT_FRAMEWORK_VERSIONS_UNIQUE_VALIDATION";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
