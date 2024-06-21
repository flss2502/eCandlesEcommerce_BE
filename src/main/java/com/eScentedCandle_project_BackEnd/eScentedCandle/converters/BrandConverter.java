package com.eScentedCandle_project_BackEnd.eScentedCandle.converters;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.BrandDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {
    public static BrandDto toDto(Brand entity){
        BrandDto  dto = new BrandDto();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setImage(entity.getImage());
        return dto;
    }
}
