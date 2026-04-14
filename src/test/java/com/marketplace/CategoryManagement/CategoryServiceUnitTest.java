package com.marketplace.CategoryManagement;

import com.marketplace.Auth.domain.JwtUtil;
import com.marketplace.CategoryManagement.api.CategoryRequestDto;
import com.marketplace.CategoryManagement.domain.Category;
import com.marketplace.CategoryManagement.domain.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CategoryServiceUnitTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private String name;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    protected void setup() {
        name = "category_test";
        category = new Category(name);
    }

    @RepeatedTest(3)
    public void saveCategory_andReturnCategory() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(name);
        Category category1 = new Category(categoryRequestDto.categoryName());
        Category savedCategory = categoryService.saveCategoryTest(category1);
        assertThat(savedCategory.getName()).isEqualTo(name);
        assertThat(savedCategory.getId()).isNotNull();
        //        Category category1 = categoryService.createCategory(categoryRequestDto);
//        assertThat(category1.getName()).isEqualTo(name);
    }
}
