package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.components.LocalizationUtils;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CategoryTypeDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.CategoryType;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.CategoryTypeResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.CategoryTypeService;
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
@RequestMapping("${api.prefix}/category_type")
@CrossOrigin
public class CategoryTypeController {
    private final CategoryTypeService categoryTypeService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<?> createCategoryType(
            @Valid @RequestBody CategoryTypeDto categoryTypeDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            categoryTypeService.createCategoryType(categoryTypeDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            //.data(categoryType)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseObject<Object>> getAllCategoryTypeByCategoryId(
            @PathVariable Long categoryId
    ) {
        try {
            List<CategoryTypeResponse> categoryType = categoryTypeService.getAllCategoryTypeByCategoryId(categoryId);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(categoryType)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryType(
            @PathVariable Long id,
            @Valid @RequestBody CategoryTypeDto categoryTypeDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            categoryTypeService.updateCategoryType(id, categoryTypeDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            //.data(categoryType)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject<Object>> getAllCategoryType() {
        try {
            List<CategoryTypeResponse> categoryType = categoryTypeService.getAllCategoryType();
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(categoryType)
                            .build()
            );
        } catch (Exception e) {
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
    public ResponseEntity<ResponseObject<Object>> deleteCategoryType(
            @PathVariable Long id
    ) {
        try {
            CategoryType categoryType =  categoryTypeService.removeCategoryType(id);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(categoryType)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error " + e.getMessage())
                            .error(e.getMessage())
                            .build()
            );
        }
    }
}
