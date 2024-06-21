package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInformationDto {
    private Long id;
    private Double weight;
    @JsonProperty("ingredient_type")
    private String ingredientType;
    private String brand;
    @JsonProperty("from_product")
    private Integer fromProduct;
    @JsonProperty("net_quantity")
    private Double netQuantity;
    @JsonProperty("product_dimension")
    private String productDimension;
    private String asin;
    @JsonProperty("generic_name")
    private String genericName;
}
