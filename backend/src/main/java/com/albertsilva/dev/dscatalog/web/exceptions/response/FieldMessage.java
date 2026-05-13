package com.albertsilva.dev.dscatalog.web.exceptions.response;

public record FieldMessage(
    String fieldName,
    String message) {
}