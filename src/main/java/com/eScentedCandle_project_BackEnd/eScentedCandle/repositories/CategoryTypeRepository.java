package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryTypeRepository extends JpaRepository<CategoryType, Long> {
    List<CategoryType> findAllByCategoryId(Long categoryId);
}
