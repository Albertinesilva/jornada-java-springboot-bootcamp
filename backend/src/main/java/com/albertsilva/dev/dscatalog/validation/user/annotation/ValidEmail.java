package com.albertsilva.dev.dscatalog.validation.user.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.albertsilva.dev.dscatalog.validation.user.validator.ValidEmailValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ValidEmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidEmail {

    String message() default "Favor informar um email válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}