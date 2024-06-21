package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCallbackDto {
    private Long orderID;

}
