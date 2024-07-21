package org.vasaviyuvajanasangha.kvcl.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Email(message="Please provide a valid email address")
@Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ExtendedEmailValidator {
    String message() default "Please provide a valid email address";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}