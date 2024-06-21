package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CreatePaymentLinkRequestBody;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;

public interface PaymentSerivce {
    ResponseEntity<ObjectNode> createOrder(CreatePaymentLinkRequestBody requestBody);
}
