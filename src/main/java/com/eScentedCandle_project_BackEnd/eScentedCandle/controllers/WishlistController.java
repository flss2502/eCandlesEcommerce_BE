package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.WishListConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.WishlistDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Wishlist;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.WishListResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.WishListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/wish_list")
@CrossOrigin
public class WishlistController {
    private final WishListService wishListService;

    @PostMapping("")
    public ResponseEntity<?> createWishList(
            @Valid @RequestBody WishlistDto wishlistDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Wishlist wishlist = wishListService.createWishlist(wishlistDto);
            WishListResponse wishListResponse = WishListConverter.toResponse(wishlist);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(wishListResponse)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWishList(@PathVariable Long id) throws DataNotFoundException {
        try {
            wishListService.deleteWishList(id);
            return ResponseEntity.ok("Delete successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Object>> getProductWishListByUser(
            @PathVariable Long id
    ) {
        try {
            List<Wishlist> wishlists = wishListService.getProductWishListByUser(id);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(wishlists)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error product " + e.getMessage())
                            .error(e.getMessage())
                            .build()
            );
        }
    }
}
