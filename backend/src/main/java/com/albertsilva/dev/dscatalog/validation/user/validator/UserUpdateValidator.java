package com.albertsilva.dev.dscatalog.validation.user.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.HandlerMapping;

import com.albertsilva.dev.dscatalog.dto.user.request.UserUpdateRequest;
import com.albertsilva.dev.dscatalog.repository.UserRepository;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UserUpdateValid;
import com.albertsilva.dev.dscatalog.web.exception.response.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateRequest> {

  private static final int MIN_TOKEN_LENGTH = 3;

  private final UserRepository repository;
  private final HttpServletRequest request;

  public UserUpdateValidator(UserRepository repository, HttpServletRequest request) {
    this.repository = repository;
    this.request = request;
  }

  @Override
  public boolean isValid(UserUpdateRequest dto, ConstraintValidatorContext context) {

    List<FieldMessage> errors = new ArrayList<>();

    validateUniqueEmail(dto, errors);
    validatePasswordDoesNotContainPersonalData(dto, errors);

    addErrors(errors, context);

    return errors.isEmpty();
  }

  private void validateUniqueEmail(UserUpdateRequest dto, List<FieldMessage> errors) {

    if (dto.email() == null || dto.email().isBlank()) {
      return;
    }

    @SuppressWarnings("unchecked")
    Map<String, String> uriVars = (Map<String, String>) request
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (uriVars == null || !uriVars.containsKey("id")) {
      return;
    }

    Long userId;

    try {

      userId = Long.parseLong(uriVars.get("id"));

    } catch (NumberFormatException e) {

      return;
    }

    String normalizedEmail = dto.email().trim().toLowerCase();

    boolean emailAlreadyExists = repository.existsByEmailIgnoreCaseAndIdNot(normalizedEmail, userId);

    if (emailAlreadyExists) {
      errors.add(new FieldMessage("email", "Email já cadastrado"));
    }
  }

  private void validatePasswordDoesNotContainPersonalData(UserUpdateRequest dto, List<FieldMessage> errors) {

    if (dto.password() == null || dto.password().isBlank()) {
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