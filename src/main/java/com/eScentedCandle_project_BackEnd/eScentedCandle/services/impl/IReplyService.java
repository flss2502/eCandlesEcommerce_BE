package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ReplyDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Feedback;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Reply;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.User;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.FeedbackRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.ReplyRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.UserRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.ReplyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IReplyService implements ReplyService {
    private final ReplyRepository replyRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Reply createReply(Long feeddbackId, ReplyDto replyDto) throws DataNotFoundException {
        Feedback feedback = feedbackRepository.findById(feeddbackId)
                .orElseThrow(() -> new DataNotFoundException("Feedback not found!!"));
        User user = userRepository.findById(replyDto.getUserId()).orElseThrow(() -> new DataNotFoundException("User not found"));
        modelMapper.typeMap(ReplyDto.class, Reply.class).addMappings(mappers -> mappers.skip(Reply::setId));
        Reply reply = new Reply();
        modelMapper.map(replyDto, reply);
        reply.setUser(user);
        reply.setFeedback(feedback);
        return replyRepository.save(reply);
    }
}
