package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.common.WebCommon;
import com.eScentedCandle_project_BackEnd.eScentedCandle.components.LocalizationUtils;
import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.CategoryConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Address;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Category;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.CategoryRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.CategoryService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ICategoryService implements CategoryService {
    private final LocalizationUtils localizationUtils;
    private final CategoryRepository categoryRepository;

    @Override
    public void createCategory(CategoryDto categoryDto) {
        String generatedCode = WebCommon.generateCodeFromName(categoryDto.getName());
        Category category = CategoryConverter.toEntity(categoryDto);
        category.setCode(generatedCode);
        category = categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Long id, CategoryDto categoryDto) {
        String generatedCode = WebCommon.generateCodeFromName(categoryDto.getName());
        Category category = new Category();
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category oldCategory = existingCategory.get();
            category = CategoryConverter.toEntity(categoryDto, oldCategory);
            category.setCode(generatedCode);
        }
        category = categoryRepository.save(category);
        CategoryConverter.toDto(category);
    }

    @Override
    public void updateCategoryImage(Long id, String imageCategory) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found!!!"));
        existingCategory.setImgUrl(imageCategory);
        categoryRepository.save(existingCategory);
    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> result = new ArrayList<>();
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getAllAndCategoryType() {
        return categoryRepository.findAll();
    }

    @Override
    public Category removeCategory(Long id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        localizationUtils
                                .getLocalizedMessage(MessageKeys.FOUND_ID_CATEGORY_FAILED)));
        categoryRepository.delete(category);
        return Category.builder()
                .name(category.getName())
                .imgUrl(category.getImgUrl())
                .id(category.getId())
                .code(category.getCode())
                .build();
    }
}
