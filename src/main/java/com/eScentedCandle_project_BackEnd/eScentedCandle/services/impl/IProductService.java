package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.common.WebCommon;
import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.ProductConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductImageDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductUpdateDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.*;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.*;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ProductResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class IProductService implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final BrandRepository brandRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductInformationRepository productInformationRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Product getProductById(long id) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new DataNotFoundException("Cannot find product with id =" + id);
    }

    @Override
    @Transactional
    public void createProduct(ProductDto productDto) throws DataNotFoundException {
        String generatedCode = WebCommon.generateCodeFromName(productDto.getProductName());
        Product product = ProductConverter.toEntity(productDto);
        float discount = productDto.getDiscount() != null ? productDto.getDiscount() : (float) 0.0;
        float priceSale = product.getCost();
        float price = priceSale * ((100 - discount) / 100);
        product.setPrice(price);
        product.setProductCode(generatedCode);
        //product.setBrand(brand);
        product.setStatus(true);

        List<Long> categoryTypeIds = productDto.getCategoryTypeIds();
        List<CategoryType> categoryTypes = new ArrayList<>();
        for (Long categoryId : categoryTypeIds) {
            CategoryType categoryType = categoryTypeRepository.findById(categoryId)
                    .orElseThrow(() -> new DataNotFoundException("CategoryType not found with id: " + categoryId));
            categoryTypes.add(categoryType);
        }
        product = productRepository.save(product);
        product.getCategoryTypes().addAll(categoryTypes);
        product = productRepository.save(product);
        if (productDto.getProductImages() != null && !productDto.getProductImages().isEmpty()) {
            List<ProductImages> productImages = new ArrayList<>();
            List<ProductImageDto> newImages = productDto.getProductImages();
            List<ProductImages> existingImages = product.getProductImages();
            int currentImageCount = (existingImages != null) ? existingImages.size() : 0;
            int maxImageCount = 5;
            int newImageCount = Math.min(maxImageCount - currentImageCount, newImages.size());
            if (newImages.size() > newImageCount) {
                throw new DataNotFoundException("Exceeded maximum allowed images");
            }
            String firstImageUrl = null;
            for (int i = 0; i < newImageCount; i++) {
                ProductImageDto imageDto = newImages.get(i);
                ProductImages productImage = new ProductImages();
                productImage.setProduct(product);
                productImage.setImageUrl(imageDto.getImageUrl());
                productImages.add(productImage);
                if (i == 0) {
                    firstImageUrl = imageDto.getImageUrl();
                }
            }
            product.setThumbnail(firstImageUrl);
            productImageRepository.saveAll(productImages);
            product.setProductImages(productImages);
            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    public void updateProduct(Long id, ProductUpdateDto productDto) throws Exception {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            String generatedCode = WebCommon.generateCodeFromName(productDto.getProductName());
            //Brand existingBrand = brandRepository.findById(productDto.getBrand()).orElseThrow(() -> new DataNotFoundException("Brand not found!!"));
            ProductConverter.toEntityUpdate(productDto, existingProduct);
            existingProduct.setProductCode(generatedCode);
            //existingProduct.setBrand(existingBrand);
            float discount = productDto.getDiscount() != null ? productDto.getDiscount() : (float) 0.0;
            float priceSale = existingProduct.getCost();
            float price = priceSale * ((100 - discount) / 100);
            existingProduct.setPrice(price);
            existingProduct = productRepository.save(existingProduct);
            List<Long> categoryTypeIds = productDto.getCategoryTypeIds();
            List<CategoryType> categoryTypes = new ArrayList<>();
            for (Long categoryId : categoryTypeIds) {
                CategoryType categoryType = categoryTypeRepository.findById(categoryId)
                        .orElseThrow(() -> new DataNotFoundException("CategoryType not found with id: " + categoryId));
                categoryTypes.add(categoryType);
            }
            existingProduct.setCategoryTypes(categoryTypes);
            existingProduct = productRepository.save(existingProduct);

//            if (productDto.getProductImages() != null && !productDto.getProductImages().isEmpty()) {
//                String firstImageUrl = null;
//                List<ProductImages> existingImages = existingProduct.getProductImages();
//                for (ProductImageDto imageDto : productDto.getProductImages()) {
//                    if (imageDto.getId() != null) {
//                        ProductImages existingImage = productImageRepository.findById(imageDto.getId()).orElseThrow(() -> new DataNotFoundException("Image not found with id: " + imageDto.getId()));
//                        if (imageDto.getImageUrl() == null || imageDto.getImageUrl().isEmpty()) {
//                            imageDto.setImageUrl(existingImage.getImageUrl());
//                        } else {
//                            existingImage.setImageUrl(imageDto.getImageUrl());
//                        }
//                        productImageRepository.save(existingImage);
//                        if (firstImageUrl == null) {
//                            firstImageUrl = imageDto.getImageUrl();
//                        }
//                    } else {
//                        ProductImages newImage = new ProductImages();
//                        newImage.setProduct(existingProduct);
//                        if (imageDto.getImageUrl() == null || imageDto.getImageUrl().isEmpty()) {
//                            if (existingImages != null && !existingImages.isEmpty()) {
//                                newImage.setImageUrl(existingImages.get(0).getImageUrl());
//                            } else {
//                                continue;
//                            }
//                        } else {
//                            newImage.setImageUrl(imageDto.getImageUrl());
//                        }
//                        productImageRepository.save(newImage);
//                        if (firstImageUrl == null) {
//                            firstImageUrl = newImage.getImageUrl();
//                        }
//                    }
//                }
//                if (firstImageUrl != null) {
//                    existingProduct.setThumbnail(firstImageUrl);
//                    productRepository.save(existingProduct);
//                }
//            }
        }
    }

    @Override
    public void deleteProduct(Long id) throws Exception {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
        productRepository.delete(existingProduct);
    }

    @Override
    public void deleteProductById(Long id) throws Exception {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
        existingProduct.setStatus(false);
        productRepository.save(existingProduct);
    }

    @Override
    public Page<ProductResponse> getAllProduct(String keyWord, PageRequest pageRequest) {
        Page<Product> products;
        if (keyWord == null || keyWord.isEmpty()) {
            products = productRepository.findAllByStatusTrue(pageRequest);
            return products.map(ProductConverter::toResponse);
        } else {
            products = productRepository.findAllByProductNameContainingIgnoreCaseAndStatusTrue(keyWord, pageRequest);
            return products.map(ProductConverter::toResponse);
        }
    }


    @Override
    public Page<ProductResponse> getAllProductFilter(String keyword, PageRequest pageRequest,
                                                     Float minPrice, Float maxPrice,
                                                     List<Long> categoryTypeIds) {
        Page<Product> products;
        products = productRepository.filterProducts(
                keyword, pageRequest, minPrice, maxPrice, categoryTypeIds);
        return products.map(product -> {
            ProductResponse response = ProductConverter.toResponse(product);
            return response;
        });
    }

    @Override
    public ProductResponse getProductResponseById(long id) throws DataNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found!!!"));
        ProductResponse response = ProductConverter.toResponse(product);
        return response;
    }

    @Override
    public List<Product> getTop10NewProduct() {
        return productRepository.findFirst10ByStatusTrueOrderByCreatedAtDesc();
    }

    @Override
    public List<Product> getRelatedProduct(long id) {
        return productRepository.findProductsByProductId(id);
    }

    @Override
    public List<Object[]> getTop5DailyBestSellingProducts() {
        return orderDetailRepository.findTop5DailyBestSellingProducts();
    }

    @Override
    public Page<ProductResponse> getProductByCategoryId(long id, PageRequest pageRequest) {
        Page<Product> products = productRepository.findByCategoryId(id, pageRequest);
        return products.map(ProductConverter::toResponse);
    }
}
//                return products.map(product -> {
//                    return ProductConverter.toResponse(product);
//        });
//        return products.map(product -> {
//            ProductResponse productResponse = ProductConverter.toResponse(product);
//            return productResponse;
//        });

