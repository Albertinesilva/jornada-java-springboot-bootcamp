package com.albertsilva.dev.dscatalog.validation.user.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.albertsilva.dev.dscatalog.dto.user.request.UserUpdateRequest;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UserUpdateValid;
import com.albertsilva.dev.dscatalog.web.exceptions.response.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateRequest> {

  @Override
  public boolean isValid(UserUpdateRequest dto, ConstraintValidatorContext context) {

    List<FieldMessage> errors = new ArrayList<>();

    validatePasswordDoesNotContainName(dto, errors);

    addErrors(errors, context);

    return errors.isEmpty();
  }

  private void validatePasswordDoesNotContainName(UserUpdateRequest dto, List<FieldMessage> errors) {

    if (dto.password() == null || dto.firstName() == null) {
      return;
    }

    String password = dto.password().toLowerCase();
    String firstName = dto.firstName().toLowerCase();

    if (password.contains(firstName)) {

      errors.add(new FieldMessage("password", "Senha não pode conter o primeiro nome"));
    }
  }

  private void addErrors(List<FieldMessage> errors, ConstraintValidatorContext context) {

    for (FieldMessage error : errors) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(error.message()).addPropertyNode(error.fieldName())
          .addConstraintViolation();
    }
  }
}