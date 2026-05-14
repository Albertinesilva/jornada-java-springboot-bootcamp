package com.albertsilva.dev.dscatalog.dto.user.request;

import java.util.Set;

import com.albertsilva.dev.dscatalog.validation.user.annotation.StrongPassword;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UniqueEmail;
import com.albertsilva.dev.dscatalog.validation.user.annotation.UserUpdateValid;

import jakarta.validation.constraints.Email;
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
        @Email(message = "Favor informar um email válido")
        @UniqueEmail
        String email,

        @StrongPassword
        String password,

        @NotEmpty(message = "Usuário deve possuir ao menos uma role")
        Set<Long> roleIds) { 
}
