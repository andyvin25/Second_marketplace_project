package com.marketplace.Auth.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record UserLoginDto(
        @NotBlank(message = "Invalid email: empty null")
        @Email(message = "Invalid Email")
        String email,

        @NotBlank(message = "password can't be empty")
//        @Size(min = 8, max = 20, message = "the password character should be between 8 and 20")
        String password) {
}
