package com.albertsilva.dev.dscatalog.validation.category.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.albertsilva.dev.dscatalog.dto.category.request.CategoryCreateRequest;
import com.albertsilva.dev.dscatalog.repository.CategoryRepository;
import com.albertsilva.dev.dscatalog.validation.category.annotation.CategoryCreateValid;
import com.albertsilva.dev.dscatalog.web.exception.response.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class CategoryCreateValidator implements ConstraintValidator<CategoryCreateValid, CategoryCreateRequest> {

  private final CategoryRepository repository;

  public CategoryCreateValidator(CategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean isValid(CategoryCreateRequest dto, ConstraintValidatorContext context) {

    List<FieldMessage> errors = new ArrayList<>();

    validateUniqueName(dto, errors);

    addErrors(errors, context);

    return errors.isEmpty();
  }

  private void validateUniqueName(CategoryCreateRequest dto, List<FieldMessage> errors) {

    if (dto.name() == null || dto.name().isBlank()) {
      return;
    }

    String normalizedName = dto.name().trim().toLowerCase();

    boolean categoryAlreadyExists = repository.existsByNameIgnoreCase(normalizedName);

    if (categoryAlreadyExists) {

      errors.add(new FieldMessage("name", "Já existe uma categoria com este nome"));
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