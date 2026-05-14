package com.albertsilva.dev.dscatalog.validation.user.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import com.albertsilva.dev.dscatalog.validation.user.validator.UniqueEmailValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface UniqueEmail {

    String message() default "Email já existente";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}