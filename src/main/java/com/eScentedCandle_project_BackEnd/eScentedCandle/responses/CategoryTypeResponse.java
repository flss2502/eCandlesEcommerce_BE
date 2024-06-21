package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryTypeResponse {
    private Long id;
    private String code;
    private String name;
    //private String imgUrl;
    private CategoryDto categoryDto;
}
