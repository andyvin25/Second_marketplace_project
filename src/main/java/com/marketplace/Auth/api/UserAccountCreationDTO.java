package com.marketplace.Auth.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@RegisterReflectionForBinding
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserAccountCreationDTO(
        @NotBlank(message = "Invalid email: empty null")
        @Email(message = "Invalid Email")
        String email,

        @NotBlank(message = "name can't be empty")
        String name,

        @NotBlank(message = "password can't be empty")
        @Size(min = 8, max = 20, message = "the password character should be between 8 and 20")
        String password,

        @NotBlank(message = "repeatPassword not the same as password")
        @Size(min = 8, max = 20, message = "the password character should be between 8 and 20")
        String repeatPassword
){
}
