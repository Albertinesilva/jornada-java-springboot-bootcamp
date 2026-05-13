package com.albertsilva.dev.dscatalog.dto.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para requisições de criação de categoria.
 *
 * <p>
 * Representa os dados que o cliente deve fornecer para cadastrar
 * uma nova categoria no sistema.
 * </p>
 *
 * <p>
 * <b>Observações:</b>
 * </p>
 * <ul>
 * <li>O campo {@code active} é opcional</li>
 * <li>Caso {@code active} não seja informado, será considerado
 * {@code false}</li>
 * </ul>
 *
 * @param name        nome da categoria
 * @param description descrição da categoria
 * @param active      indica se a categoria será criada como ativa
 */
public record CategoryCreateRequest(

        @NotBlank(message = "O nome da categoria é obrigatório") 
        @Size(min = 3, max = 80, message = "O nome da categoria deve ter entre 3 e 80 caracteres") 
        String name,

        @Pattern(regexp = "^$|^.{3,255}$", message = "A descrição deve ter entre 3 e 255 caracteres")
        String description,

        Boolean active

) {
}