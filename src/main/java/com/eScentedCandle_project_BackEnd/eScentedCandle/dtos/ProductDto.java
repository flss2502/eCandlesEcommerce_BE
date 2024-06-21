package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Brand;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.CategoryType;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.ProductInformation;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.AvailabilityProductEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    //private Long id;
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    @JsonProperty("product_name")
    private String productName;
    //private String thumbnail;
    @Min(value = 1, message = "Cost must be greater than or equal to 0")
    @JsonProperty("cost")
    private Float cost;
    //    @Min(value = 0, message = "Price must be greater than or equal to 0")
    //    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    //    private Float price;
    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    private Float discount;
    //    @JsonProperty("product_code")
    //    private String productCode;
    private AvailabilityProductEnum availability;
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private int quantity;
    private Boolean status;
    //private String type;
    @NotBlank(message = "Product name is required")
    @JsonProperty("product_dimensions")
    private String productDimensions;
    @NotBlank(message = "Product name is required")
    @JsonProperty("product_detail")
    private String productDetail;
    //    @JsonProperty("brand_id")
    //    private Long brand;
    @JsonProperty("category_type_id")
    private List<Long> categoryTypeIds;
    private List<ProductImageDto> productImages;
}
