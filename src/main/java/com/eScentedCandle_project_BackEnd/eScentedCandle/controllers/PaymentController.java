package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.PaymentCallbackDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.PaymentSuccess;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.OrderService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.PaymentSerivce;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lib.payos.PayOS;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController

@RequestMapping("api/v1/payment")
public class PaymentController {
    private final PayOS payOS;
    private final OrderService orderService;
    private final PaymentSerivce paymentService;

    public PaymentController(PayOS payOS, PaymentSerivce paymentService, OrderService orderService) {
        this.payOS = payOS;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/success/{orderID}")
    public ResponseEntity<PaymentSuccess> processPaymentSuccessCallback(@PathVariable Long orderID) {
        return orderService.updatePaymentStatus(orderID);
    }

    @PostMapping(value = "/cancel/{orderID}")
    public ResponseEntity<PaymentSuccess> processPaymentCancelCallback(@PathVariable Long orderID) {

        return orderService.cancelOrder(orderID);
    }


    @PostMapping("/payos-transfer-handler")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            // Init Response
            response.put("error", 0);
            response.put("message", "Ok");
            response.set("data", null);

            JsonNode data = payOS.verifyPaymentWebhookData(body);

            System.out.println(data);

            // Kiểm tra data.get("description") không phải null trước khi gọi asText()
            if (data != null && data.get("description") != null && Objects.equals(data.get("description").asText(), "Ma giao dich thu nghiem")) {
                return response;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }


}
