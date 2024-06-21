package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.components.LocalizationUtils;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductImageDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ProductUpdateDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Product;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ProductCreateResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ProductListResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ProductResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.ProductImageService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.ProductRedisService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
@CrossOrigin
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final LocalizationUtils localizationUtils;
    //private final ProductRedisService productRedisService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto, BindingResult result) throws DataNotFoundException {
        try {
            ProductCreateResponse productCreateResponse = new ProductCreateResponse();
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                productCreateResponse.setMessage("Insert failed");
                productCreateResponse.setErrors(errorMessages);
                return ResponseEntity.ok().body(productCreateResponse);
            }
            productService.createProduct(productDto);
//            productCreateResponse.setStatus("success");
//            productCreateResponse.setProduct(Product);
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.CREATED.value()).message("Successfully")
                    //.data(product)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error " + e.getMessage()).error(e.getMessage()).build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateDto productDTO, BindingResult result) throws Exception {
        try {
            ProductCreateResponse productCreateResponse = new ProductCreateResponse();
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                productCreateResponse.setMessage("Insert Failed");
                productCreateResponse.setErrors(errorMessages);
                return ResponseEntity.ok().body(productCreateResponse);
            }
            productService.updateProduct(id, productDTO);
            //productCreateResponse.setProduct(product);
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.ACCEPTED.value()).message("Successfully")
                    //.data(product)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error ").error(e.getMessage()).build());
        }
    }

//    @DeleteMapping("/delete-product/{id}")
//    public ResponseEntity<?> deleteProduct(@PathVariable Long id) throws DataNotFoundException {
//        try {
//            productService.deleteProduct(id);
//            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.ACCEPTED.value()).message("Successfully").build());
//        } catch (Exception e) {
//            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error ").error(e.getMessage()).build());
//        }
//    }
    @DeleteMapping("/product_image/{imageId}")
    public ResponseEntity<?> deleteProductImage(@PathVariable Long imageId) throws DataNotFoundException {
        try {
            productImageService.deleteProductImages(imageId);
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.ACCEPTED.value()).message("Successfully").build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error ").error(e.getMessage()).build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id) throws DataNotFoundException {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.ACCEPTED.value()).message("Successfully").build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error ").error(e.getMessage()).build());
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject<Object>> getAllProductByProductNameAndPaging(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "10") int limit) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<ProductResponse> productPage = productService.getAllProduct(keyword, pageRequest);
        return getResponseObjectResponseEntity(productPage);
    }


    @GetMapping("/filter")
    public ResponseEntity<ResponseObject<Object>> getAllProduct(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(value = "minPrice", required = false) Float minPrice, @RequestParam(value = "maxPrice", required = false) Float maxPrice,
                                                                //@RequestParam(value = "brandIds", required = false) String brandIds,
                                                                @RequestParam(value = "categoryTypeIds", required = false) String categoryTypeIds) {
        //List<Long> parsedBrandIds = parseIds(brandIds);
        List<Long> parsedCategoryTypeIds = parseIds(categoryTypeIds);
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<ProductResponse> productPage = productService.getAllProductFilter(keyword, pageRequest, minPrice, maxPrice, parsedCategoryTypeIds);
        return getResponseObjectResponseEntity(productPage);
    }

    @GetMapping("/product_category/{id}")
    public ResponseEntity<ResponseObject<Object>> getProductByCategory(@PathVariable Long id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<ProductResponse> productResponses = productService.getProductByCategoryId(id, pageRequest);
        return getResponseObjectResponseEntity(productResponses);
    }

    @GetMapping("/top_10_new_product")
    public ResponseEntity<ResponseObject<Object>> getTop10NewProduct() {
        try {
            List<Product> product = productService.getTop10NewProduct();
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.OK.value()).message("Successfully").data(product).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error product " + e.getMessage()).error(e.getMessage()).build());
        }
    }

    @GetMapping("/related_product/{id}")
    public ResponseEntity<ResponseObject<Object>> getRelatedProduct(@PathVariable Long id) {
        try {
            List<Product> product = productService.getRelatedProduct(id);
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.OK.value()).message("Successfully").data(product).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error product " + e.getMessage()).error(e.getMessage()).build());
        }
    }

    @GetMapping("/top_5_daily_best_selling")
    public ResponseEntity<ResponseObject<Object>> getTop5DailyBestSellingProducts() {
        try {
            List<Object[]> product = productService.getTop5DailyBestSellingProducts();
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.OK.value()).message("Successfully").data(product).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error product ").error(e.getMessage()).build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Object>> getProductResponseById(@PathVariable long id) {
        try {
            ProductResponse productResponse = productService.getProductResponseById(id);
            return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.OK.value()).message("Successfully").data(productResponse).build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message("Error product ").error(e.getMessage()).build());
        }
    }

    @PutMapping("/image_product/{productId}")
    public ResponseEntity<ResponseObject<Object>> updateProductImages(
            @PathVariable Long productId,
            @RequestBody List<ProductImageDto> imageDtos,
            BindingResult result
    ) {
        try {
            ResponseObject<Object> response = new ResponseObject<>();
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                response.setMessage("Update Failed");
                response.setError(String.join(", ", errorMessages));
                return ResponseEntity.ok().body(response);
            }
            productImageService.updateProductImages(productId, imageDtos);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.ACCEPTED.value())
                            .message("Successfully")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }



    private List<Long> parseIds(String ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
    }

//    @DeleteMapping("/products/clear")
//    public ResponseEntity<String> clearProductCache() {
//        try {
//            productRedisService.clear();
//            return ResponseEntity.ok("Cache cleared successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error occurred while clearing cache: " + e.getMessage());
//        }
//    }

    private ResponseEntity<ResponseObject<Object>> getResponseObjectResponseEntity(Page<ProductResponse> productPage) {
        int totalPages = productPage.getTotalPages();
        Long totalProduct = productPage.getTotalElements();
        List<ProductResponse> products = productPage.getContent();
        ProductListResponse productListResponse = ProductListResponse.builder().products(products).totalProducts(totalProduct).totalPages(totalPages).build();
        return ResponseEntity.ok(ResponseObject.builder().statusCode(HttpStatus.OK.value()).message("Successfully").data(productListResponse).build());
    }
}
