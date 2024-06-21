package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.FeedbackDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(FeedbackDto feedbackDto) throws DataNotFoundException;

    Page<Feedback> getAllFeedback(PageRequest pageRequest);
}
