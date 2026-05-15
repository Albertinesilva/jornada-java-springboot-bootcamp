package com.albertsilva.dev.dscatalog.validation.category.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.albertsilva.dev.dscatalog.dto.category.request.CategoryUpdateRequest;
import com.albertsilva.dev.dscatalog.repository.CategoryRepository;
import com.albertsilva.dev.dscatalog.validation.category.annotation.CategoryUpdateValid;
import com.albertsilva.dev.dscatalog.web.exception.response.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class CategoryUpdateValidator implements ConstraintValidator<CategoryUpdateValid, CategoryUpdateRequest> {

  private final CategoryRepository repository;
  private final HttpServletRequest request;

  public CategoryUpdateValidator(CategoryRepository repository, HttpServletRequest request) {
    this.repository = repository;
    this.request = request;
  }

  @Override
  public boolean isValid(CategoryUpdateRequest dto, ConstraintValidatorContext context) {

    List<FieldMessage> errors = new ArrayList<>();

    validateUniqueName(dto, errors);

    addErrors(errors, context);

    return errors.isEmpty();
  }

  private void validateUniqueName(CategoryUpdateRequest dto, List<FieldMessage> errors) {

    if (dto.name() == null || dto.name().isBlank()) {
      return;
    }

    Map<String, String> uriVars = (Map<String, String>) request
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (uriVars == null || !uriVars.containsKey("id")) {
      return;
    }

    Long categoryId = Long.parseLong(uriVars.get("id"));

    String normalizedName = dto.name().trim().toLowerCase();

    boolean categoryAlreadyExists = repository.existsByNameIgnoreCaseAndIdNot(normalizedName, categoryId);

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