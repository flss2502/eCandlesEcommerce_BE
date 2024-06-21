package com.eScentedCandle_project_BackEnd.eScentedCandle.converters;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductUpdateDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Product;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.AvailabilityProductEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public static ProductDto toDto(Product entity) {
        ProductDto dto = new ProductDto();
        //dto.setId(dto.getId());
        dto.setCost(entity.getCost());
        dto.setProductName(entity.getProductName());
        //dto.setPrice(entity.getPrice());
        dto.setDiscount(entity.getDiscount());
        //dto.setProductCode(entity.getProductCode());
        dto.setAvailability(AvailabilityProductEnum.valueOf(entity.getAvailability()));
        dto.setStatus(entity.getStatus());
        //dto.setType(entity.getType());
        //dto.setThumbnail(entity.getThumbnail());
        dto.setProductDetail(entity.getProductDetail());
        return dto;
    }

    public static Product toEntity(ProductDto dto){
        Product entity = new Product();
        //entity.setId(dto.getId());
        entity.setCost(dto.getCost());
        entity.setProductName(dto.getProductName());
        //entity.setPrice(dto.getPrice());
        entity.setDiscount(dto.getDiscount());
        //entity.setProductCode(dto.getProductCode());
        entity.setAvailability(String.valueOf(dto.getAvailability()));
        entity.setProductDimensions(dto.getProductDimensions());
        entity.setQuantity(dto.getQuantity());
        entity.setStatus(dto.getStatus());
        //entity.setThumbnail(dto.getThumbnail());
        //entity.setType(dto.getType());
        entity.setProductDetail(dto.getProductDetail());
        return entity;
    }

    public static Product toEntity(ProductDto dto, Product entity){
        //entity.setId(dto.getId());
        entity.setCost(dto.getCost());
        entity.setProductName(dto.getProductName());
        //entity.setPrice(dto.getPrice());
        entity.setDiscount(dto.getDiscount());
        //entity.setProductCode(dto.getProductCode());
        entity.setProductDimensions(dto.getProductDimensions());
        entity.setAvailability(String.valueOf(dto.getAvailability()));
        entity.setStatus(dto.getStatus());
        //entity.setThumbnail(dto.getThumbnail());
        //entity.setType(dto.getType());
        entity.setProductDetail(dto.getProductDetail());
        return entity;
    }

    public static Product toEntityUpdate(ProductUpdateDto dto, Product entity){
        //entity.setId(dto.getId());
        entity.setCost(dto.getCost());
        entity.setProductName(dto.getProductName());
        //entity.setPrice(dto.getPrice());
        entity.setDiscount(dto.getDiscount());
        //entity.setProductCode(dto.getProductCode());
        entity.setProductDimensions(dto.getProductDimensions());
        entity.setAvailability(String.valueOf(dto.getAvailability()));
        entity.setStatus(dto.getStatus());
        //entity.setThumbnail(dto.getThumbnail());
        //entity.setType(dto.getType());
        entity.setProductDetail(dto.getProductDetail());
        return entity;
    }

    public static ProductResponse toResponse(Product entity){
        ProductResponse productResponse = ProductResponse.builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .thumbnail(entity.getThumbnail())
                .cost(entity.getCost())
                .price(entity.getPrice())
                .discount(entity.getDiscount())
                .productCode(entity.getProductCode())
                .availability(entity.getAvailability())
                .quantity(entity.getQuantity())
                .status(entity.getStatus())
                //.type(entity.getType())
                .productDetail(entity.getProductDetail())
                //.brand(entity.getBrand())
                .categoryType(entity.getCategoryTypes())
                .productImages(entity.getProductImages())
                .productDimensions(entity.getProductDimensions())
                .build();
        productResponse.setCreatedAt(entity.getCreatedAt());
        productResponse.setUpdatedAt(entity.getUpdatedAt());
        return productResponse;
    }
}
