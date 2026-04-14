package com.marketplace.CategoryManagement.domain;

import com.marketplace.CategoryManagement.api.CategoryDto;
import com.marketplace.CategoryManagement.api.CategoryRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public Category saveCategoryTest(Category category) {
        categoryRepository.save(category);
        return category;
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.getCategories();
    }

    public void createCategory(CategoryRequestDto categoryDto) {
        boolean checkIsThatTheSameCategory = getCategories().stream()
                .anyMatch(categoryDto1 -> categoryDto.categoryName().equals(categoryDto1.name()));
        if (checkIsThatTheSameCategory) {
            throw new IllegalArgumentException("You have already send the same category in to db");
        }
        Category category = new Category(categoryDto.categoryName());
        categoryRepository.save(category);
    }

    public void createCategories(List<CategoryRequestDto> categoryRequestDtos) {
        Map<String, Long> frequencyMap = categoryRequestDtos.stream()
                .collect(Collectors.groupingBy(
                        dto -> dto.categoryName().toLowerCase(),
                        Collectors.counting()));

        // Step 2: Check for duplicates within input DTOs
        String duplicatedElements = categoryRequestDtos.stream()
                .map(CategoryRequestDto::categoryName)
                .filter(name -> frequencyMap.get(name.toLowerCase()) > 1)
                .collect(Collectors.joining(", "));

        if (!duplicatedElements.isBlank()) {
            throw new IllegalArgumentException("There are duplicated categories in the input: " + duplicatedElements);
        }


        // Step 3: Get lowercase category names from input DTOs
        List<String> categoryNameRequestDtosList = categoryRequestDtos.stream()
                .map(categoryRequestDto -> categoryRequestDto.categoryName().toLowerCase())
                .toList();

        // Step 4: Find duplicates between input DTOs and database categories
        String dbDuplicates = getCategories().stream()
                .map(CategoryDto::name)
                .filter(name -> categoryNameRequestDtosList.contains(name.toLowerCase()))
                .collect(Collectors.joining(", "));

        // Step 5: Throw exception if duplicates are found in the database
        if (!dbDuplicates.isBlank()) {
            throw new IllegalArgumentException("Categories already exist in the database: " + dbDuplicates);
        }

        List<Category> categories = categoryRequestDtos.stream()
                .map(s -> new Category(s.categoryName().toLowerCase()))
                .toList();
        if (categories.size() < 2) {
            Category category = new Category(categories.getFirst().getName());
            categoryRepository.save(category);
            return;
        }

        for (int i = 0; i < categories.size(); i++) {
            Category category = new Category(categories.get(i).getName());
            categoryRepository.save(category);

        }
    }

}
