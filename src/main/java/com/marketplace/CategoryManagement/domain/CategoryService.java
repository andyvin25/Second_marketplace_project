package com.marketplace.CategoryManagement.domain;

import com.marketplace.CategoryManagement.api.CategoryDto;
import com.marketplace.CategoryManagement.api.CategoryRequestDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories();
    void createCategory(CategoryRequestDto categoryDto);
    void createCategories(List<CategoryRequestDto> categoryRequestDtos);
}
