package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.OrderDetail;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.OrderDetailRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IOrderDetailService implements OrderDetailService {
 private final OrderDetailRepository orderDetailRepository;
}
