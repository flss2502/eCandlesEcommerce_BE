package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDto {
    private Long id;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;

    private String imageUrls;

    private String status;

    //private List<ReplyDto> replies;

    @NotNull(message = "ProductId is required")
    private Long productId;

    private Long parentId;

    private Long userId;

//    private Long replyId;

//    private Long replierId;

    //private String userFullName;
}
