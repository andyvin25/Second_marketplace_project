package com.marketplace.Sub_Category_Management.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record SubCategoryDto(String id, String name) {
}
