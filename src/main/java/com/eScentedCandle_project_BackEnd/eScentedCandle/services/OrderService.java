package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CreatePaymentLinkRequestBody;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.OrderDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.OrderStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.OrderResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.PaymentSuccess;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.SumTotalOrderResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.TotalOrderReponse;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface OrderService {
    OrderResponse createOrder(OrderDto orderDto) throws DataNotFoundException;

    OrderResponse updateOrder(Long id, OrderStatusEnum orderStatus, PaymentStatusEnum paymentStatus) throws DataNotFoundException;

    Page<OrderResponse> getAllOrders(PageRequest pageRequest) throws DataNotFoundException;

    Page<OrderResponse> getOrdersByPaymentStatusAndOrderStatus(PaymentStatusEnum paymentStatus, OrderStatusEnum orderStatus, PageRequest pageRequest);

    Page<OrderResponse> getProductByUserId(PageRequest pageRequest) throws DataNotFoundException;

    OrderResponse getOrderById(Long id) throws DataNotFoundException;

    TotalOrderReponse getTotalOrder(LocalDate startDate, LocalDate endDate);

    SumTotalOrderResponse getSumTotalOrder(LocalDate startDate, LocalDate endDate);


    ResponseEntity<ObjectNode> createOrderQR(OrderDto orderDto);

    ResponseEntity<PaymentSuccess> updatePaymentStatus(Long orderID);

    ResponseEntity<PaymentSuccess> cancelOrder(Long orderID);
}
