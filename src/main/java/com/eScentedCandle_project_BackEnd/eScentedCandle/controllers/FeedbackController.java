package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.FeedbackDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Feedback;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.FeedbackListResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/feedback")
@CrossOrigin
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("")
    public ResponseEntity<?> createFeedback(
            @Valid @RequestBody FeedbackDto feedbackDto,
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
            Feedback feedback = feedbackService.createFeedback(feedbackDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(feedback)
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


    @GetMapping("/get_all")
    public ResponseEntity<ResponseObject<Object>> getAllFeedback(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").descending()
        );
        Page<Feedback> feedbacks = feedbackService.getAllFeedback(pageRequest);
        int totalPages = feedbacks.getTotalPages();
        Long totalProduct = feedbacks.getTotalElements();
        List<Feedback> feedbackList = feedbacks.getContent();
        FeedbackListResponse feedbackListResponse = FeedbackListResponse.builder()
                .feedbacks(feedbackList)
                .totalPages(totalPages)
                .totalFeedback(totalProduct)
                .build();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully")
                        .data(feedbackListResponse)
                        .build()
        );
    }
}
