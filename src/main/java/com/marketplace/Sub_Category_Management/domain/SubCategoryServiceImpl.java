package com.marketplace.Sub_Category_Management.domain;

import com.marketplace.Sub_Category_Management.api.SubCategoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

@Service("subCategory_SubCategoryManagement")
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
    public List<SubCategoryDto> getSubCategories() {
        return subCategoryRepository.getSubCategories();
    }

    @Override
    public void createSubCategory(String subCategoryName, Category category) {
        boolean checkIsThatTheSameSubCategoryName = getSubCategories().stream()
                .anyMatch(subCategoryDto -> subCategoryDto.name().equals(subCategoryName));
        if (checkIsThatTheSameSubCategoryName) {
            throw new IllegalArgumentException("You have already create subCategory");
        }
        SubCategory subCategory = new SubCategory(subCategoryName, category);
        subCategoryRepository.save(subCategory);
    }

    @Override
    public void createSubCategories(List<String> subCategoryNames, Category category) {
        Map<String, Long> frequencyMap = subCategoryNames.stream()
                .collect(Collectors.groupingBy(
                        String::toLowerCase,
                        Collectors.counting()));

        // Step 2: Check for duplicates within input DTOs
        String duplicateElements = subCategoryNames.stream()
                .map(String::toLowerCase)
                .filter(name -> frequencyMap.get(name.toLowerCase()) > 1)
                .collect(Collectors.joining(", "));

        if (!duplicateElements.isBlank()) {
            throw new IllegalArgumentException("There are duplicated categories in the input: " + duplicateElements);
        }

        List<String> subCategoryNameRequestDtosList = subCategoryNames.stream()
                .map(String::toLowerCase)
                .toList();

        String dbDuplicates = getSubCategories().stream()
                .map(SubCategoryDto::name)
                .filter(name -> subCategoryNameRequestDtosList.contains(name.toLowerCase()))
                .collect(Collectors.joining());

        if (!dbDuplicates.isBlank()) {
            throw new IllegalArgumentException("Categories already exist in the database: " + dbDuplicates);
        }

        List<SubCategory> subCategories = null;
        subCategories = subCategoryNames.stream()
                .map(s -> new SubCategory(s, category))
                .toList();

        if (subCategories.size() < 2) {
            SubCategory subCategory = new SubCategory(subCategories.getFirst().getSubCategoryName(), category);
            Set<SubCategory> subCategories1 = new HashSet<>();
            subCategories1.add(subCategory);
            category.setSubCategories(subCategories1);
            subCategoryRepository.save(subCategory);
            return;
        }

        for (int i = 0; i < subCategories.size(); i++) {
            SubCategory subCategory = new SubCategory(subCategories.get(i).getSubCategoryName(), category);
            Set<SubCategory> subCategories1 = new HashSet<>();
            subCategories1.add(subCategory);
            category.setSubCategories(subCategories1);
            subCategoryRepository.save(subCategory);
        }

    }
}
