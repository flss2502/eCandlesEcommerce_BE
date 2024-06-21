package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryTypeDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.CategoryType;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.CategoryTypeResponse;

import java.util.List;

public interface CategoryTypeService {
    void createCategoryType(CategoryTypeDto categoryTypeDto) throws DataNotFoundException;

    void updateCategoryType(Long id, CategoryTypeDto categoryTypeDto) throws DataNotFoundException;

    CategoryType removeCategoryType(Long id) throws DataNotFoundException;

    List<CategoryTypeResponse> getAllCategoryType();
    List<CategoryTypeResponse> getAllCategoryTypeByCategoryId(Long categoryId) throws DataNotFoundException;
}
