package com.eScentedCandle_project_BackEnd.eScentedCandle.converters;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.CategoryType;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.CategoryTypeResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryTypeConverter {
    public static CategoryTypeResponse toResponse(CategoryType entity) {
        CategoryDto dto = CategoryDto.builder()
                //.code(entity.getCode())
                .name(entity.getName())
                .id(entity.getId())
                .build();
        CategoryTypeResponse response = CategoryTypeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .categoryDto(dto)
                .build();
        return response;
    }
}
