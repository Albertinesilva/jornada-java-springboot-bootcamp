package com.albertsilva.dev.dscatalog.web.exception.response;

public record FieldMessage(
    String fieldName,
    String message) {
}