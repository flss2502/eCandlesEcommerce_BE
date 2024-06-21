package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductImageDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;

import java.util.List;

public interface ProductImageService {
    void updateProductImages(Long productId, List<ProductImageDto> imageDtos) throws DataNotFoundException;

    void deleteProductImages(Long id) throws DataNotFoundException;
}
