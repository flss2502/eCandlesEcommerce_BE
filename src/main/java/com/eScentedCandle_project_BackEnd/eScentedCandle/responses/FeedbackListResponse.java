package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Feedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class FeedbackListResponse {
    private List<Feedback> feedbacks;
    private int totalPages;
    private Long totalFeedback;
}
