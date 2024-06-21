package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductImageDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Product;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.ProductImages;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.ProductImageRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.ProductRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.ProductImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IProductImageService implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void updateProductImages(Long productId, List<ProductImageDto> imageDtos) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found!!!!"));

        List<ProductImages> existingProductImages = productImageRepository.findByProductId(productId);
        int currentImageCount = existingProductImages.size();
        int maxImageCount = 5;

        // Check if the total number of images after adding new ones exceeds the max limit
        int newImageCount = (int) imageDtos.stream().filter(dto -> dto.getId() == null || dto.getId() == 0).count();
        if (currentImageCount + newImageCount > maxImageCount) {
            throw new DataNotFoundException("Cannot add more images. Maximum limit of 5 images reached.");
        }

        for (ProductImageDto imageDto : imageDtos) {
            ProductImages productImage;

            if (imageDto.getId() == null || imageDto.getId() == 0) {
                // Create new ProductImages entity
                productImage = new ProductImages();
            } else {
                // Update existing ProductImages entity
                productImage = productImageRepository.findById(imageDto.getId())
                        .orElseThrow(() -> new DataNotFoundException("Image not found with id: " + imageDto.getId()));
            }

            // Map the DTO to the product image entity
            productImage.setImageUrl(imageDto.getImageUrl());
            productImage.setProduct(existingProduct);
            productImageRepository.save(productImage);
        }

        // After saving images, set the first image as the thumbnail
        List<ProductImages> updatedProductImages = productImageRepository.findByProductId(productId);
        if (!updatedProductImages.isEmpty()) {
            String firstImageUrl = updatedProductImages.get(0).getImageUrl();
            existingProduct.setThumbnail(firstImageUrl);
            productRepository.save(existingProduct);
        }
    }


    @Override
    public void deleteProductImages(Long id) throws DataNotFoundException {
        ProductImages productImages = productImageRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product image not found!!!"));
        productImageRepository.delete(productImages);
    }

}
