package com.marketplace.SubCategoryManagement;

import com.marketplace.Sub_Category_Management.domain.Category;
import com.marketplace.Sub_Category_Management.domain.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CategoryServiceUnitTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;
    String categoryName;

    @BeforeEach
    void setup() {
        categoryName = "test_category";
    }

    @RepeatedTest(3)
    public void saveCategoryUnitTest() {
        Category savedCategory = categoryService.saveCategoryTest(categoryName);
        Category getCategory = categoryService.getCategoryByName(categoryName);

        assertThat(savedCategory.getName()).isEqualTo(categoryName);
        assertThat(getCategory.getName()).isEqualTo(categoryName);
    }

}
