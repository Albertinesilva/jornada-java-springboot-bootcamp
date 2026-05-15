package com.albertsilva.dev.dscatalog.validation.role.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.albertsilva.dev.dscatalog.validation.role.validator.ValidRolesValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ValidRolesValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoles {

  String message() default "Roles inválidas";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}