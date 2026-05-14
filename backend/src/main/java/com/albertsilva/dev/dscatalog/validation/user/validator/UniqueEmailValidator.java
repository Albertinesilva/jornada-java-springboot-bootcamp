package com.albertsilva.dev.dscatalog.validation.user.validator;

import org.springframework.stereotype.Component;

import com.albertsilva.dev.dscatalog.repositories.UserRepository;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UniqueEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator
        implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository repository;

    public UniqueEmailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return true;
        }

        String normalizedEmail = value.trim().toLowerCase();

        return !repository.existsByEmailIgnoreCase(normalizedEmail);
    }
}
