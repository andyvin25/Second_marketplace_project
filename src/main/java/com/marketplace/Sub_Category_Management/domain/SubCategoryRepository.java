package com.marketplace.Sub_Category_Management.domain;

import com.marketplace.Sub_Category_Management.api.SubCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {

    @Query("SELECT new com.marketplace.Sub_Category_Management.api.SubCategoryDto(s.id, s.subCategoryName) FROM Sub_Category_SubCategoryManagement s")
    List<SubCategoryDto> getSubCategories();
}
