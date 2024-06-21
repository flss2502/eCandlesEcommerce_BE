package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.WishlistDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Wishlist;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishListService {
    Wishlist createWishlist(WishlistDto wishlistDto) throws DataNotFoundException;

    List<Wishlist> getProductWishListByUser(Long id);

    void deleteWishList(Long id) throws DataNotFoundException;
}
