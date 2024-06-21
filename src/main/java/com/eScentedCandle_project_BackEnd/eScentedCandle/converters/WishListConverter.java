package com.eScentedCandle_project_BackEnd.eScentedCandle.converters;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Wishlist;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.WishListResponse;
import org.springframework.stereotype.Component;

@Component
public class WishListConverter {
    public static WishListResponse toResponse(Wishlist entity) {
        return WishListResponse.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .userId(entity.getUser().getId())
                .build();
    }
}
