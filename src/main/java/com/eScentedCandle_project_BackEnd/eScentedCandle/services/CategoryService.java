package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Category;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryDto categoryDto);

    void updateCategory(Long id, CategoryDto categoryDto);
    void updateCategoryImage(Long id, String imageCategory) throws DataNotFoundException;

    List<Category> getAllCategory();

    List<Category> getAllAndCategoryType();

    Category removeCategory(Long id) throws DataNotFoundException;
}
