package com.wei.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({ FIELD, ANNOTATION_TYPE })
@Constraint(validatedBy=EmailValidator.class)
public @interface ValidEmail {
	String message() default "Invalid Email";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
