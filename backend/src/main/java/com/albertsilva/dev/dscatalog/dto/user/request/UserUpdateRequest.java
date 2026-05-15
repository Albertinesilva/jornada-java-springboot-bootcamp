package com.albertsilva.dev.dscatalog.dto.user.request;

import java.util.Set;

import com.albertsilva.dev.dscatalog.validation.role.annotation.ValidRoles;
import com.albertsilva.dev.dscatalog.validation.user.annotation.StrongPassword;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UserUpdateValid;
import com.albertsilva.dev.dscatalog.validation.user.annotation.ValidEmail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@UserUpdateValid
public record UserUpdateRequest(

        @NotBlank(message = "Primeiro nome é obrigatório")
        @Size(min = 2, max = 80)
        String firstName,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(min = 2, max = 80)
        String lastName,

        @NotBlank(message = "Email é obrigatório")
        @ValidEmail
        String email,

        @StrongPassword
        String password,

        @NotEmpty(message = "Usuário deve possuir ao menos uma role")
        @ValidRoles
        Set<Long> roleIds) { 
}
