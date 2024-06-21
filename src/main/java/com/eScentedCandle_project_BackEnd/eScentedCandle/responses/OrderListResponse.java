package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class OrderListResponse {
    private List<Order> orders;
    private int totalPages;
    private Long totalProducts;
}
