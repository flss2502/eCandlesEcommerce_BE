package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ReplyDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Reply;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.ReplyService;
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
@RequestMapping("${api.prefix}/reply")
@CrossOrigin
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/{feedbackId}")
    public ResponseEntity<?> createReply(
            @PathVariable Long feedbackId,
            @Valid @RequestBody ReplyDto replyDto,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Reply reply = replyService.createReply(feedbackId,replyDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(reply)
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
}
