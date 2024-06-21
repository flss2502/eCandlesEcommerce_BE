package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDto {
    private Long id;
    private String code;
    @NotBlank(message = "Name is required")
    private String name;
    private String image;
}
