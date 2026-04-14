package com.marketplace.UserAccountManagement.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@RegisterReflectionForBinding
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserAccountDTO(
        String id,
        String email,
        String userName
) {
}