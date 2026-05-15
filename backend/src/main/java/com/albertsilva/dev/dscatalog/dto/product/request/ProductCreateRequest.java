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
 * DTO utilizado para requisições de criação
 * de produtos.
 *
 * <p>
 * Representa os dados fornecidos pelo cliente
 * durante o processo de cadastro de um novo produto
 * no sistema.
 *
 * <p>
 * Este DTO utiliza a annotation
 * {@link ProductCreateValid} para executar
 * validações contextuais relacionadas ao processo
 * de criação do produto.
 *
 * <p>
 * As validações aplicadas garantem:
 * <ul>
 * <li>Obrigatoriedade do nome do produto</li>
 * <li>Tamanho mínimo e máximo permitido</li>
 * <li>Validação de caracteres permitidos</li>
 * <li>Validação do preço</li>
 * <li>Validação da URL da imagem</li>
 * <li>Validação da data</li>
 * <li>Validação das categorias associadas</li>
 * </ul>
 *
 * <p>
 * As categorias relacionadas ao produto são
 * enviadas apenas pelos seus identificadores.
 *
 * <p>
 * Exemplo:
 * 
 * <pre>{@code
 * "categoryIds": [1, 2, 3]
 * }</pre>
 *
 * <p>
 * As regras de validação utilizam Bean Validation
 * através das annotations presentes nos atributos
 * do record.
 *
 * @param name        nome do produto
 * @param description descrição do produto
 * @param price       preço do produto
 * @param imgUrl      URL da imagem do produto
 * @param date        data associada ao produto
 * @param categoryIds lista contendo os identificadores
 *                    das categorias do produto
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
