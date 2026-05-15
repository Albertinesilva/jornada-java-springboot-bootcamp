package com.albertsilva.dev.dscatalog.dto.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para requisições de criação de categorias.
 *
 * <p>
 * Representa os dados fornecidos pelo cliente
 * durante o processo de cadastro de uma nova
 * categoria no sistema.
 *
 * <p>
 * As validações aplicadas garantem:
 * <ul>
 * <li>Obrigatoriedade do nome da categoria</li>
 * <li>Tamanho mínimo e máximo permitido</li>
 * <li>Validação de caracteres permitidos</li>
 * <li>Validação do tamanho da descrição</li>
 * </ul>
 *
 * <p>
 * As regras de validação utilizam Bean Validation
 * através das annotations presentes nos atributos
 * do record.
 *
 * @param name        nome da categoria
 * @param description descrição da categoria
 */
public record CategoryCreateRequest(

    @NotBlank(message = "O nome da categoria é obrigatório") 
    @Size(min = 3, max = 80, message = "O nome da categoria deve ter entre 3 e 80 caracteres") 
    @Pattern(regexp = "^[A-Za-zÀ-ÿ0-9\\s]+$", message = "O nome da categoria possui caracteres inválidos") 
    String name,

    @Pattern(regexp = "^$|^.{3,255}$", message = "A descrição deve ter entre 3 e 255 caracteres") 
    String description) {
}
