package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryTypeDto {
    private Long id;
    //private String code;
    @NotBlank(message = "Name is required")
    private String name;
    @JsonProperty("category_id")
    private Long categoryId;
    //private String image;
}
