package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.components.LocalizationUtils;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.BrandDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Brand;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.BrandService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.utils.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@CrossOrigin
@RequestMapping("${api.prefix}/brand")
@Tag(name = "Brand Controller")
public class BrandController {
    private final LocalizationUtils localizationUtils;
    private final BrandService brandService;

    @Operation(summary = "Add new brand", description = "API create new brand")
    @PostMapping("")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBrand(
            @Valid @RequestBody BrandDto brandDto,
            BindingResult result
    ) {
        try {
            //Brand brand = new Brand();
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            brandService.createbrand(brandDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .message("Successfully")
                            //.data(brand)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error product ")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    //@ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandDto brandDto,
            BindingResult result
    ) throws DataNotFoundException {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            brandService.updateBrand(id, brandDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.ACCEPTED.value())
                            .message("Successfully")
                            //.data(brand)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error product ")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseObject<Object>> brands() {
        List<Brand> brand = brandService.getAllBrand();
        try {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(brand)
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

    @DeleteMapping("/{id}")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteBrand(
            @PathVariable Long id
    ) throws DataNotFoundException {
        try {
            brandService.removeBrand(id);
            return ResponseEntity.ok(localizationUtils
                    .getLocalizedMessage(MessageKeys.DELETE_BRAND_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
