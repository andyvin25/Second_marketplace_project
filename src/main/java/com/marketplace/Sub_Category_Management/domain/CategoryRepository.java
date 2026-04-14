package com.marketplace.Sub_Category_Management.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("CategoryRepository_SubCategoryManagement")
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c FROM Category_Sub_Category_Management c WHERE c.name = :category_name")
    Optional<Category> findCategoriesByName(@Param("category_name") String categoryName);
}
