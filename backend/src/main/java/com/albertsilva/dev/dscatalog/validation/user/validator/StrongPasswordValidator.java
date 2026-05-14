package com.albertsilva.dev.dscatalog.validation.user.validator;

import org.springframework.stereotype.Component;

import com.albertsilva.dev.dscatalog.validation.user.annotation.StrongPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final String PATTERN = "^(?=\\S+$)" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*\\d)" +
            "(?=.*[^a-zA-Z0-9])" +
            ".{10,}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false;
        }

        return value.matches(PATTERN);
    }
}