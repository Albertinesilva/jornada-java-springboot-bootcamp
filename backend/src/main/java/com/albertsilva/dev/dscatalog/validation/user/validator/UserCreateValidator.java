package com.albertsilva.dev.dscatalog.validation.user.validator;

import java.util.ArrayList;
import java.util.List;

import com.albertsilva.dev.dscatalog.dto.user.request.UserCreateRequest;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UserCreateValid;
import com.albertsilva.dev.dscatalog.web.exception.response.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserCreateValidator implements ConstraintValidator<UserCreateValid, UserCreateRequest> {

    private static final int MIN_TOKEN_LENGTH = 3;

    @Override
    public boolean isValid(UserCreateRequest dto, ConstraintValidatorContext context) {

        List<FieldMessage> errors = new ArrayList<>();

        validatePasswordDoesNotContainPersonalData(dto, errors);

        addErrors(errors, context);

        return errors.isEmpty();
    }

    private void validatePasswordDoesNotContainPersonalData(UserCreateRequest dto, List<FieldMessage> errors) {

        if (dto.password() == null) {
            return;
        }

        String password = dto.password().trim().toLowerCase();

        validateToken(password, dto.firstName(), errors);
        validateToken(password, dto.lastName(), errors);

        if (dto.email() != null && dto.email().contains("@")) {

            String emailPrefix = dto.email().split("@")[0];

            validateToken(password, emailPrefix, errors);
        }
    }

    private void validateToken(String password, String value, List<FieldMessage> errors) {

        if (value == null) {
            return;
        }

        String normalized = value.trim().toLowerCase();

        if (normalized.length() < MIN_TOKEN_LENGTH) {
            return;
        }

        boolean alreadyExists = errors.stream().anyMatch(error -> error.fieldName().equals("password")
                && error.message().equals("Senha não pode conter dados pessoais"));

        if (password.contains(normalized) && !alreadyExists) {
            errors.add(new FieldMessage("password", "Senha não pode conter dados pessoais"));
        }
    }

    private void addErrors(List<FieldMessage> errors, ConstraintValidatorContext context) {

        if (errors.isEmpty()) {
            return;
        }

        context.disableDefaultConstraintViolation();

        for (FieldMessage error : errors) {

            context.buildConstraintViolationWithTemplate(error.message()).addPropertyNode(error.fieldName())
                    .addConstraintViolation();
        }
    }
}