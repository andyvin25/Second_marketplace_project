package com.marketplace.CategoryManagement.domain;

import com.marketplace.CategoryManagement.api.CategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CategoryRepository_CategoryManagement")
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT new com.marketplace.CategoryManagement.api.CategoryDto(c.id, c.name) FROM Category_Category_Management c")
    List<CategoryDto> getCategories();
}
