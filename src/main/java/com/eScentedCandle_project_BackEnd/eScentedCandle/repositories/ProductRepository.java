package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByProductNameContainingIgnoreCaseAndStatusTrue(String name, Pageable pageable);
    Page<Product> findAllByStatusTrue(Pageable pageable);
    Product getProductById(Long productId);
    List<Product> findFirst10ByStatusTrueOrderByCreatedAtDesc();

    @Query("SELECT p FROM Product p " +
            "JOIN p.categoryTypes ct " +
            "WHERE p.status = true " +
            "AND (:keyword IS NULL OR :keyword = '' OR p.productName LIKE %:keyword% OR p.productDetail LIKE %:keyword%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:categoryTypeIds IS NULL OR ct.id IN :categoryTypeIds)")
    Page<Product> filterProducts(
            @Param("keyword") String keyword, Pageable pageable,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice,
            @Param("categoryTypeIds") List<Long> categoryTypeIds);

    @Query("SELECT p FROM Product p " +
            "JOIN p.categoryTypes ct " +
            "WHERE p.id = :productId " +
            "AND p.status = true")
    List<Product> findProductsByProductId(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p " +
            "JOIN p.categoryTypes ct " +
            "WHERE ct.category.id = :categoryId " +
            "AND p.status = true")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
