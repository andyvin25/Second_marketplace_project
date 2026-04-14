package com.marketplace.Sub_Category_Management.domain;

import com.marketplace.Exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

@Service("Category_CategoryServiceImpl_SubCategoryManagement")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findCategoriesByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("There is no Category with this name " + categoryName));
    }

    public Category saveCategoryTest(String categoryName) {
        Set<SubCategory> subCategories = new HashSet<>();
        Category category = Category.builder()
                .name(categoryName)
                .createdAt(LocalDateTime.now())
                .build();
        SubCategory subCategory = new SubCategory("test_subCategory", category);
        subCategories.add(subCategory);
        category.setSubCategories(subCategories);
        categoryRepository.save(category);
        return category;
    }
}
