package com.albertsilva.dev.dscatalog.validation.user.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.albertsilva.dev.dscatalog.validation.user.validator.UserUpdateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UserUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface UserUpdateValid {
  String message() default "Validation error";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
