package com.marketplace.ProductEtalationManagement.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EtalationRequestDto(
    @NotBlank(message = "Fill the etalation name in the field")
    @Size(max = 150, min = 1)
    String etalationName
) {
}
