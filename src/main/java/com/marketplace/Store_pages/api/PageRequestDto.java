package com.marketplace.Store_pages.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PageRequestDto(
    @NotBlank(message = "the name shouldn't be empty")
    @Size(min = 2, max = 20, message = "the name range minimum is 5 and max is 20 characters")
    String pageName
) {}