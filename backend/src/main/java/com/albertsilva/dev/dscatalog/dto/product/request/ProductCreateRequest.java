package com.albertsilva.dev.dscatalog.dto.product.request;

import java.time.Instant;
import java.util.List;

import com.albertsilva.dev.dscatalog.validation.product.annotation.ProductCreateValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para criação de produtos.
 *
 * <p>
 * <b>Importante sobre categorias:</b>
 * </p>
 * <ul>
 * <li>As categorias são enviadas apenas como IDs</li>
 * <li>Exemplo: {@code "categoryIds": [1, 2, 3]}</li>
 * <li>O sistema irá buscar essas categorias no banco</li>
 * </ul>
 *
 * <p>
 * <b>Exemplo de JSON:</b>
 * </p>
 * 
 * <pre>
 * {
 *   "name": "Notebook",
 *   "description": "Notebook gamer",
 *   "price": 4500.0,
 *   "imgUrl": "http://image.com",
 *   "date": "2025-01-01T10:00:00Z",
 *   "categoryIds": [1, 2]
 * }
 * </pre>
 *
 * @param name        nome do produto
 * @param description descrição do produto
 * @param price       preço
 * @param imgUrl      URL da imagem
 * @param date        data associada
 * @param categoryIds lista de IDs das categorias
 */
@ProductCreateValid
public record ProductCreateRequest(

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
