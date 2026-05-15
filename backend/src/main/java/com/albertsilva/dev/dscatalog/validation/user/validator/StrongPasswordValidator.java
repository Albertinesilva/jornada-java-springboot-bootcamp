package com.albertsilva.dev.dscatalog.validation.user.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.albertsilva.dev.dscatalog.validation.user.annotation.StrongPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final int MIN_LENGTH = 10;

    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");

    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");

    private static final Pattern NUMBER_PATTERN = Pattern.compile(".*\\d.*");

    private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile(".*[^a-zA-Z0-9\\s].*");

    private static final Set<String> COMMON_PASSWORDS = Set.of("123456", "12345678", "password", "admin", "qwerty",
            "abc123", "111111", "123123");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        List<String> errors = new ArrayList<>();

        validateBlank(value, errors);

        if (!errors.isEmpty()) {
            addErrors(errors, context);
            return false;
        }

        validateWhitespace(value, errors);
        validateLength(value, errors);
        validateUppercase(value, errors);
        validateLowercase(value, errors);
        validateNumber(value, errors);
        validateSpecialCharacter(value, errors);
        validateCommonPasswords(value, errors);

        addErrors(errors, context);

        return errors.isEmpty();
    }

    private void validateBlank(String password, List<String> errors) {

        if (password.isBlank()) {
            errors.add("Senha não pode ser vazia");
        }
    }

    private void validateWhitespace(String password, List<String> errors) {

        boolean containsWhitespace = password.chars().anyMatch(Character::isWhitespace);

        if (containsWhitespace) {
            errors.add("Senha não pode conter espaços");
        }
    }

    private void validateLength(String password, List<String> errors) {

        if (password.length() < MIN_LENGTH) {
            errors.add(String.format("Senha deve possuir ao menos %d caracteres", MIN_LENGTH));
        }
    }

    private void validateUppercase(String password, List<String> errors) {

        if (!UPPERCASE_PATTERN.matcher(password).matches()) {
            errors.add("Senha deve conter ao menos uma letra maiúscula");
        }
    }

    private void validateLowercase(String password, List<String> errors) {

        if (!LOWERCASE_PATTERN.matcher(password).matches()) {
            errors.add("Senha deve conter ao menos uma letra minúscula");
        }
    }

    private void validateNumber(String password, List<String> errors) {

        if (!NUMBER_PATTERN.matcher(password).matches()) {
            errors.add("Senha deve conter ao menos um número");
        }
    }

    private void validateSpecialCharacter(String password, List<String> errors) {

        if (!SPECIAL_CHARACTER_PATTERN.matcher(password).matches()) {
            errors.add("Senha deve conter ao menos um caractere especial");
        }
    }

    private void validateCommonPasswords(String password, List<String> errors) {

        String normalizedPassword = password.toLowerCase();

        boolean containsForbiddenPattern = COMMON_PASSWORDS.stream().anyMatch(normalizedPassword::contains);

        if (containsForbiddenPattern) {
            errors.add("Senha contém padrões muito comuns e inseguros");
        }
    }

    private void addErrors(List<String> errors, ConstraintValidatorContext context) {

        if (errors.isEmpty()) {
            return;
        }

        context.disableDefaultConstraintViolation();

        for (String error : errors) {
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        }
    }
}