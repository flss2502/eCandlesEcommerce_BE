package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.ReplyDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Reply;

public interface ReplyService {
    Reply createReply(Long feeddbackId, ReplyDto replyDto) throws DataNotFoundException;
}
