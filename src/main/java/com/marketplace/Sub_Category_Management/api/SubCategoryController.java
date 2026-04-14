package com.marketplace.Sub_Category_Management.api;

import com.marketplace.Sub_Category_Management.domain.Category;
import com.marketplace.Sub_Category_Management.domain.CategoryServiceImpl;
import com.marketplace.Sub_Category_Management.domain.SubCategoryServiceImpl;
import com.marketplace.Util.ResponseHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class SubCategoryController {

    private final CategoryServiceImpl categoryService;
    private final SubCategoryServiceImpl subCategoryService;

//    @PostMapping("/admin/sub_category")
//    public ResponseEntity<Object> createSubCategories(@RequestParam("category") String categoryName, @RequestBody SubCategoryRequestDto subCategoryDto) {
//        Category category = categoryService.getCategoryByName(categoryName);
//        if (category == null) {
//            throw new IllegalArgumentException("There is no Category with the computer name");
//        }
//        subCategoryService.createSubCategory(subCategoryDto.subCategoryName(), category);
//        return ResponseHandler.generateResponse("Successfully create sub category", HttpStatus.OK, "");
//    }

    @PostMapping("/admin/many_sub_category")
    public ResponseEntity<Object> createManySubCategories(@RequestParam("category") String categoryName, @RequestBody List<SubCategoryRequestDto> subCategoryName) {
        System.out.println("subCategoryName = " + subCategoryName);
        Category category = categoryService.getCategoryByName(categoryName);
        List<String> subCategoryNames = subCategoryName.stream()
                        .map(SubCategoryRequestDto::subCategoryName)
                        .toList();
        subCategoryService.createSubCategories(subCategoryNames, category);
        return ResponseHandler.generateResponse("Successfully create sub categories", HttpStatus.OK, "");

    }

}
