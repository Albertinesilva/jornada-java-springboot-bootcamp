package com.albertsilva.dev.dscatalog.dto.product.request;

import java.time.Instant;
import java.util.List;

import com.albertsilva.dev.dscatalog.validation.product.annotation.ProductUpdateValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para atualização de produtos.
 *
 * <p>
 * Permite atualização parcial (PATCH-like).
 * </p>
 *
 * <p>
 * <b>Importante:</b>
 * </p>
 * <ul>
 * <li>Campos nulos são ignorados</li>
 * <li>Categorias continuam sendo enviadas por IDs</li>
 * </ul>
 *
 * @param name        novo nome
 * @param description nova descrição
 * @param price       novo preço
 * @param imgUrl      nova imagem
 * @param date        nova data
 * @param categoryIds novos IDs de categorias
 */
@ProductUpdateValid
public record ProductUpdateRequest(

        @NotBlank(message = "O nome do produto é obrigatório") 
        @Size(min = 3, max = 100, message = "O nome do produto deve conter entre 3 e 100 caracteres") 
        @Pattern(regexp = "^[A-Za-zÀ-ÿ0-9\\s\\-()]+$", message = "O nome do produto possui caracteres inválidos") 
        String name,

        @Size(min = 3, max = 200, message = "A descrição do produto deve conter entre 3 e 200 caracteres") 
        String description,

        @Positive(message = "O preço deve ser um valor positivo") 
        Double price,

        @Pattern(regexp = "^(https?://).+$", message = "A URL da imagem é inválida") 
        String imgUrl,

        @PastOrPresent(message = "A data deve ser no passado ou presente") 
        Instant date,

        @NotEmpty(message = "O produto deve possuir ao menos uma categoria")
        List<Long> categoryIds) {
}
