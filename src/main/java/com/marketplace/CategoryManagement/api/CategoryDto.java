package com.marketplace.CategoryManagement.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record CategoryDto(String id, String name) {
}
