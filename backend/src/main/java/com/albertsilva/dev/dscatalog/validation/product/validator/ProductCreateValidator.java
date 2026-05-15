package com.albertsilva.dev.dscatalog.validation.product.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.albertsilva.dev.dscatalog.dto.product.request.ProductCreateRequest;
import com.albertsilva.dev.dscatalog.repository.CategoryRepository;
import com.albertsilva.dev.dscatalog.repository.ProductRepository;
import com.albertsilva.dev.dscatalog.validation.product.annotation.ProductCreateValid;
import com.albertsilva.dev.dscatalog.web.exception.response.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ProductCreateValidator implements ConstraintValidator<ProductCreateValid, ProductCreateRequest> {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductCreateValidator(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public boolean isValid(ProductCreateRequest dto, ConstraintValidatorContext context) {

    List<FieldMessage> errors = new ArrayList<>();

    validateUniqueName(dto, errors);
    validateCategories(dto, errors);

    addErrors(errors, context);

    return errors.isEmpty();
  }

  private void validateUniqueName(ProductCreateRequest dto, List<FieldMessage> errors) {

    if (dto.name() == null || dto.name().isBlank()) {
      return;
    }

    String normalizedName = dto.name().trim().toLowerCase();

    boolean productAlreadyExists = productRepository.existsByNameIgnoreCase(normalizedName);

    if (productAlreadyExists) {

      errors.add(new FieldMessage("name", "Já existe um produto com este nome"));
    }
  }

  private void validateCategories(ProductCreateRequest dto, List<FieldMessage> errors) {

    if (dto.categoryIds() == null || dto.categoryIds().isEmpty()) {
      return;
    }

    boolean invalidCategory = dto.categoryIds().stream().anyMatch(id -> !categoryRepository.existsById(id));

    if (invalidCategory) {

      errors.add(new FieldMessage("categoryIds", "Uma ou mais categorias informadas não existem"));
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