package com.marketplace.Sub_Category_Management.domain;

import com.marketplace.Sub_Category_Management.api.SubCategoryDto;

import java.util.List;

public interface SubCategoryService {
    List<SubCategoryDto> getSubCategories();
    void createSubCategory(String subCategoryName, Category category);
    void createSubCategories(List<String> subCategoryNames, Category category);
}
