package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.WishlistDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Product;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.User;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Wishlist;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.ProductRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.UserRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.WishlistRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.WishListService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class IWishListService implements WishListService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public Wishlist createWishlist(WishlistDto wishlistDto) throws DataNotFoundException {
        User existingUser = userRepository.findById(wishlistDto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
        Product existingProduct = productRepository.findById(wishlistDto.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found!!"));
        modelMapper.typeMap(WishlistDto.class, Wishlist.class)
                .addMappings(mappers -> mappers.skip(Wishlist::setId));
        Wishlist wishlist = new Wishlist();
        modelMapper.map(wishlistDto, wishlist);
        wishlist.setUser(existingUser);
        wishlist.setProduct(existingProduct);
        wishlist = wishlistRepository.save(wishlist);
        return wishlist;
    }

    @Override
    public List<Wishlist> getProductWishListByUser(Long id) {
        return wishlistRepository.findByUserId(id);
    }

    @Override
    public void deleteWishList(Long id) throws DataNotFoundException {
        Wishlist existingWishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found!!!"));
        wishlistRepository.delete(existingWishlist);
    }
}
