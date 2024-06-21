package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CreatePaymentLinkRequestBody;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.PaymentSerivce;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lib.payos.PayOS;
import com.lib.payos.type.ItemData;
import com.lib.payos.type.PaymentData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class IPaymentService implements PaymentSerivce {

    private final PayOS payOS;

    @Override
    public ResponseEntity<ObjectNode> createOrder(CreatePaymentLinkRequestBody requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            // Kiểm tra nếu requestBody là null
            if (requestBody == null) {
                response.put("error", -1);
                response.put("message", "Request body is null");
                return ResponseEntity.badRequest().body(response);
            }

            // Xử lý logic tạo đơn hàng và thanh toán ở đây
            PaymentData paymentData = new PaymentData(
                    (int) requestBody.getTotalAmount(),
                    (int) requestBody.getSubTotal(),
                    requestBody.getNotes(),  // Assuming notes as description
                    Collections.emptyList(),  // Assuming no items
                    requestBody.getCancelUrl(),
                    requestBody.getReturnUrl()
            );

            JsonNode data = payOS.createPaymentLink(paymentData);
            response.put("error", 0);
            response.put("message", "Order created successfully");
            response.set("data", data);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý nếu có lỗi xảy ra
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "Error occurred while creating order");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
