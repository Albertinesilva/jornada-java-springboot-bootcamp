package com.albertsilva.dev.dscatalog.validation.user.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import com.albertsilva.dev.dscatalog.validation.user.validator.StrongPasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

  String message() default "Senha deve conter no mínimo 10 caracteres, letra maiúscula, minúscula, número e caractere especial";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}