package com.albertsilva.dev.dscatalog.validation.role.validator;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.albertsilva.dev.dscatalog.repository.RoleRepository;
import com.albertsilva.dev.dscatalog.validation.role.annotation.ValidRoles;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ValidRolesValidator implements ConstraintValidator<ValidRoles, Set<Long>> {

  private final RoleRepository repository;

  public ValidRolesValidator(RoleRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean isValid(Set<Long> value, ConstraintValidatorContext context) {

    if (value == null || value.isEmpty()) {
      return true;
    }

    boolean allRolesExist = value.stream().allMatch(repository::existsById);

    if (allRolesExist) {
      return true;
    }

    context.disableDefaultConstraintViolation();

    context.buildConstraintViolationWithTemplate("Uma ou mais roles informadas não existem").addConstraintViolation();

    return false;
  }
}
