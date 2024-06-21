package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.common.WebCommon;
import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.CategoryConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.CategoryTypeConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryTypeDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Category;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.CategoryType;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.CategoryRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.CategoryTypeRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.CategoryTypeResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.CategoryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ICategoryTypeService implements CategoryTypeService {
    private final CategoryTypeRepository categoryTypeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void createCategoryType(CategoryTypeDto categoryTypeDto) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(categoryTypeDto.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found!!"));
        String generatedCode = WebCommon.generateCodeFromName(categoryTypeDto.getName());
        CategoryType categoryType = CategoryType.builder()
                .code(generatedCode)
                //.image(categoryTypeDto.getImage())
                .name(categoryTypeDto.getName())
                .category(existingCategory)
                .build();
        categoryTypeRepository.save(categoryType);
    }

    @Override
    public void updateCategoryType(Long id, CategoryTypeDto categoryTypeDto) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(categoryTypeDto.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found!!"));
        String generatedCode = WebCommon.generateCodeFromName(categoryTypeDto.getName());
        CategoryType existingCategoryType = categoryTypeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category type not found!!"));
        existingCategoryType.setCategory(existingCategory);
        existingCategoryType.setCode(generatedCode);
        //existingCategoryType.setImage(categoryTypeDto.getImage());
        existingCategoryType.setName(categoryTypeDto.getName());
        categoryTypeRepository.save(existingCategoryType);
    }

    @Override
    public CategoryType removeCategoryType(Long id) throws DataNotFoundException {
        CategoryType existingCategoryType = categoryTypeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category type not found!!"));
        categoryTypeRepository.delete(existingCategoryType);
        return CategoryType.builder()
                .id(existingCategoryType.getId())
                .name(existingCategoryType.getName())
                .code(existingCategoryType.getCode())
                .build();
    }

    @Override
    public List<CategoryTypeResponse> getAllCategoryType() {
        List<CategoryType> categoryTypes = categoryTypeRepository.findAll();
        return categoryTypes.stream()
                .map(categoryType -> CategoryTypeConverter.toResponse(categoryType))
                .collect(Collectors.toList());
//        return categoryTypeRepository.findAll().stream()
//                .map(CategoryTypeConverter::toResponse)
//                .toList(); // Java 16+ method
    }

    @Override
    public List<CategoryTypeResponse> getAllCategoryTypeByCategoryId(Long categoryId) throws DataNotFoundException {
        CategoryType existingCategoryType = categoryTypeRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Category type not found!!!"));
        List<CategoryType> categoryTypes = categoryTypeRepository.findAllByCategoryId(existingCategoryType.getId());
        return categoryTypes.stream()
                .map(CategoryTypeConverter::toResponse)
                .collect(Collectors.toList());
    }

}
