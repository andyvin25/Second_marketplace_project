package com.marketplace.CategoryManagement.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record CategoryRequestDto(String categoryName) {
}
