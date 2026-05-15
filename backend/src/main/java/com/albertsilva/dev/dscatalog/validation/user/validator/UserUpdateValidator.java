package com.albertsilva.dev.dscatalog.validation.user.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.albertsilva.dev.dscatalog.dto.user.request.UserUpdateRequest;
import com.albertsilva.dev.dscatalog.repository.UserRepository;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UserUpdateValid;
import com.albertsilva.dev.dscatalog.web.exception.response.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateRequest> {

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
    validatePasswordDoesNotContainName(dto, errors);

    addErrors(errors, context);

    return errors.isEmpty();
  }

  private void validateUniqueEmail(UserUpdateRequest dto, List<FieldMessage> errors) {

    if (dto.email() == null || dto.email().isBlank()) {
      return;
    }

    Map<String, String> uriVars = (Map<String, String>) request
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (uriVars == null || !uriVars.containsKey("id")) {
      return;
    }

    Long userId = Long.parseLong(uriVars.get("id"));

    String normalizedEmail = dto.email().trim().toLowerCase();

    boolean emailAlreadyExists = repository.existsByEmailIgnoreCaseAndIdNot(normalizedEmail, userId);

    if (emailAlreadyExists) {

      errors.add(new FieldMessage("email", "Email já cadastrado"));
    }
  }

  private void validatePasswordDoesNotContainName(UserUpdateRequest dto, List<FieldMessage> errors) {

    if (dto.password() == null || dto.password().isBlank() || dto.firstName() == null || dto.firstName().isBlank()) {

      return;
    }

    String password = dto.password().trim().toLowerCase();

    String firstName = dto.firstName().trim().toLowerCase();

    if (password.contains(firstName)) {

      errors.add(new FieldMessage("password", "Senha não pode conter o primeiro nome"));
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