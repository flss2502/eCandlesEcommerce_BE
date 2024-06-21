package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDto {
    private Long id;
    @JsonProperty("product_id")
    //@Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;

    //@Size(min = 5, max = 400, message = "Image's name")
    @JsonProperty("image_url")
    private String imageUrl;
}