package com.albertsilva.dev.dscatalog.validation.user.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.albertsilva.dev.dscatalog.validation.user.annotation.StrongPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final int MIN_LENGTH = 8;

    private static final Pattern UPPERCASE = Pattern.compile(".*[A-Z].*");

    private static final Pattern LOWERCASE = Pattern.compile(".*[a-z].*");

    private static final Pattern NUMBER = Pattern.compile(".*\\d.*");

    private static final Pattern SPECIAL = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

    private static final List<String> COMMON_PASSWORDS = List.of("123456", "12345678", "password", "admin", "qwerty",
            "abc123", "111111", "123123");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return true;
        }

        List<String> errors = new ArrayList<>();

        validateLength(value, errors);
        validateUppercase(value, errors);
        validateLowercase(value, errors);
        validateNumber(value, errors);
        validateSpecialCharacter(value, errors);
        validateCommonPasswords(value, errors);

        if (errors.isEmpty()) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        for (String error : errors) {
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        }

        return false;
    }

    private void validateLength(String password, List<String> errors) {

        if (password.length() < MIN_LENGTH) {
            errors.add("Senha deve possuir ao menos 8 caracteres");
        }
    }

    private void validateUppercase(String password, List<String> errors) {

        if (!UPPERCASE.matcher(password).matches()) {
            errors.add("Senha deve conter ao menos uma letra maiúscula");
        }
    }

    private void validateLowercase(String password, List<String> errors) {

        if (!LOWERCASE.matcher(password).matches()) {
            errors.add("Senha deve conter ao menos uma letra minúscula");
        }
    }

    private void validateNumber(String password, List<String> errors) {

        if (!NUMBER.matcher(password).matches()) {
            errors.add("Senha deve conter ao menos um número");
        }
    }

    private void validateSpecialCharacter(String password, List<String> errors) {

        if (!SPECIAL.matcher(password).matches()) {
            errors.add("Senha deve conter ao menos um caractere especial");
        }
    }

    private void validateCommonPasswords(String password, List<String> errors) {

        String normalizedPassword = password.toLowerCase();

        boolean containsForbiddenPattern = COMMON_PASSWORDS.stream().anyMatch(normalizedPassword::contains);

        if (containsForbiddenPattern) {
            errors.add("Senha contém padrões muito comuns");
        }
    }
}