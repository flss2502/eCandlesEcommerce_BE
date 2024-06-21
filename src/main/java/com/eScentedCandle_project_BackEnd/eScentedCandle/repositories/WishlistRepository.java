package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    //        @Query("SELECT w.product FROM Wishlist w WHERE w.user.id = :userId")
//    List<Product> findProductsByUserId(@Param("userId") Long userId);
    List<Wishlist> findByUserId(Long userId);
}
