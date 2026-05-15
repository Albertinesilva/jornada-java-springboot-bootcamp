package com.albertsilva.dev.dscatalog.dto.user.request;

import java.util.Set;

import com.albertsilva.dev.dscatalog.validation.role.annotation.ValidRoles;
import com.albertsilva.dev.dscatalog.validation.user.annotation.StrongPassword;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UniqueEmail;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UserCreateValid;
import com.albertsilva.dev.dscatalog.validation.user.annotation.ValidEmail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@UserCreateValid
public record UserCreateRequest(

        @NotBlank(message = "Primeiro nome é obrigatório")
        @Size(min = 2, max = 80, message = "Primeiro nome deve ter entre 2 e 80 caracteres")
        String firstName,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(min = 2, max = 80, message = "Sobrenome deve ter entre 2 e 80 caracteres")
        String lastName,

        @NotBlank(message = "Email é obrigatório")
        @ValidEmail
        @UniqueEmail
        String email,

        @StrongPassword
        String password,

        @NotEmpty(message = "Usuário deve possuir ao menos uma role")
        @ValidRoles
        Set<Long> roleIds) {
}
