package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;


import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSuccess {
    private int status;
    private String message;
    private OrderResponse data;

}
