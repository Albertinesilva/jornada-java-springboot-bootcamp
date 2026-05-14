package com.albertsilva.dev.dscatalog.validation.user.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.albertsilva.dev.dscatalog.dto.user.request.UserCreateRequest;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UserCreateValid;
import com.albertsilva.dev.dscatalog.web.exceptions.response.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
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

        String password = dto.password().toLowerCase();

        validateToken(password, dto.firstName(), errors);
        validateToken(password, dto.lastName(), errors);

        if (dto.email() != null) {

            String emailPrefix = dto.email().split("@")[0];

            validateToken(password, emailPrefix, errors);
        }
    }

    private void addErrors(List<FieldMessage> errors, ConstraintValidatorContext context) {

        if (!errors.isEmpty()) {
            context.disableDefaultConstraintViolation();
        }

        for (FieldMessage error : errors) {

            context.buildConstraintViolationWithTemplate(error.message()).addPropertyNode(error.fieldName())
                    .addConstraintViolation();
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

        if (password.contains(normalized)) {

            errors.add(new FieldMessage("password", "Senha não pode conter dados pessoais"));
        }
    }
}