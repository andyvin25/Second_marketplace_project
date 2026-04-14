package com.marketplace.UserAccountManagement.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@RegisterReflectionForBinding
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserAccountUpdatePasswordDTO(
    @NotBlank(message = "The Password have to contain")
    @Size(min = 8, max = 15, message = "The password should be 8 to 15 characters")
    String password, 
    @NotBlank(message = "The repeat password not the same as password")
    @Size(min = 8, max = 15, message = "The password should be 8 to 15 characters")
    String repeatPassword) { 
}
