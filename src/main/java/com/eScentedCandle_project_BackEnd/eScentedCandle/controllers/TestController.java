package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.components.LocalizationUtils;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.CustomerResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.SumTotalOrderResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.TotalOrderReponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.BrandService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/test")
public class TestController {
    private final LocalizationUtils localizationUtils;
    private final BrandService brandService;
    private final OrderService orderService;
    @GetMapping("/count_user")
    public ResponseEntity<CustomerResponse> getCountTotalCustomer(
            @RequestParam(value = "startDate") LocalDate startDate,
            @RequestParam(value = "endDate")LocalDate endDate
    ){
        CustomerResponse customerResponse = brandService.getCountTotalUser(startDate, endDate);
        return ResponseEntity.ok(customerResponse);
    }

    @GetMapping("/count_order")
    public ResponseEntity<TotalOrderReponse> getCountTotalOrder(
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)LocalDate endDate
    ){
        TotalOrderReponse totalOrderReponse = orderService.getTotalOrder(startDate, endDate);
        return ResponseEntity.ok(totalOrderReponse);
    }

    @GetMapping("/sum_total_order")
    public ResponseEntity<SumTotalOrderResponse> getSumTotalOrder(
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)LocalDate endDate
    ){
        SumTotalOrderResponse totalOrderReponse = orderService.getSumTotalOrder(startDate, endDate);
        return ResponseEntity.ok(totalOrderReponse);
    }
}
