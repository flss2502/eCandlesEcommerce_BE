package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductImageDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{
    private Long id;
    private String productName;
    private String thumbnail;
    @JsonProperty("cost")
    private Float cost;
    private Float price;
    private Float discount;
    @JsonProperty("product_code")
    private String productCode;
    private String availability;
    private int quantity;
    private Boolean status;
    private String type;
    @JsonProperty("product_dimensions")
    private String productDimensions;
    @JsonProperty( "product_detail")
    private String productDetail;
    @JsonProperty("brand_id")
    private Brand brand;
    @JsonProperty("category_type_id")
    private List<CategoryType> categoryType;
    private List<ProductImages> productImages;
    //private List<ProductInformation> productInformation;
}
