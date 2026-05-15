package com.albertsilva.dev.dscatalog.validation.product.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.albertsilva.dev.dscatalog.dto.product.request.ProductUpdateRequest;
import com.albertsilva.dev.dscatalog.repositories.CategoryRepository;
import com.albertsilva.dev.dscatalog.repositories.ProductRepository;
import com.albertsilva.dev.dscatalog.validation.product.annotation.ProductUpdateValid;
import com.albertsilva.dev.dscatalog.web.exceptions.response.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ProductUpdateValidator implements ConstraintValidator<ProductUpdateValid, ProductUpdateRequest> {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final HttpServletRequest request;

  public ProductUpdateValidator(ProductRepository productRepository, CategoryRepository categoryRepository,
      HttpServletRequest request) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.request = request;
  }

  @Override
  public boolean isValid(ProductUpdateRequest dto, ConstraintValidatorContext context) {

    List<FieldMessage> errors = new ArrayList<>();

    validateUniqueName(dto, errors);
    validateCategories(dto, errors);

    addErrors(errors, context);

    return errors.isEmpty();
  }

  private void validateUniqueName(ProductUpdateRequest dto, List<FieldMessage> errors) {

    if (dto.name() == null || dto.name().isBlank()) {
      return;
    }

    Map<String, String> uriVars = (Map<String, String>) request.getAttribute(
        HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (uriVars == null || !uriVars.containsKey("id")) {
      return;
    }

    Long productId = Long.parseLong(uriVars.get("id"));

    String normalizedName = dto.name().trim().toLowerCase();

    boolean productAlreadyExists = productRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, productId);

    if (productAlreadyExists) {

      errors.add(new FieldMessage("name", "Já existe um produto com este nome"));
    }
  }

  private void validateCategories(ProductUpdateRequest dto, List<FieldMessage> errors) {

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
