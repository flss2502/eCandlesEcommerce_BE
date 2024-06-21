package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductUpdateDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Product;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

    Product getProductById(long id) throws Exception;

    void createProduct(ProductDto productDto) throws DataNotFoundException;

    void updateProduct(Long id, ProductUpdateDto productDto) throws Exception;

    void deleteProduct(Long id) throws Exception;

    void deleteProductById(Long id) throws Exception;

    //List<ProductResponse> getProductByCategory(Long id);
    Page<ProductResponse> getAllProduct(String keyword, PageRequest pageRequest);

    Page<ProductResponse> getAllProductFilter(
            String productName, PageRequest pageRequest, Float minPrice, Float maxPrice
            , List<Long> categoryTypeIds);

    List<Product> getTop10NewProduct();

    List<Product> getRelatedProduct(long id);

    List<Object[]> getTop5DailyBestSellingProducts();

    Page<ProductResponse> getProductByCategoryId(long id, PageRequest pageRequest);

    ProductResponse getProductResponseById(long id) throws DataNotFoundException;

    //List<Top5ProductDto> getTop5BestSellingProducts();
}
