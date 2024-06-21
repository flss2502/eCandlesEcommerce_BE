package com.eScentedCandle_project_BackEnd.eScentedCandle.converters;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {
    public static CategoryDto toDto(Category entity){
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        //dto.setCode(entity.getCode());
        dto.setImgUrl(entity.getImgUrl());
        return dto;
    }

    public static Category toEntity(CategoryDto dto){
        Category entity = new Category();
        entity.setName(dto.getName());
        //entity.setCode(dto.getCode());
        entity.setImgUrl(dto.getImgUrl());
        return entity;
    }

    public static Category toEntity(CategoryDto dto, Category entity){
        entity.setName(dto.getName());
        //entity.setCode(dto.getCode());
        entity.setImgUrl(dto.getImgUrl());
        return entity;
    }
}
