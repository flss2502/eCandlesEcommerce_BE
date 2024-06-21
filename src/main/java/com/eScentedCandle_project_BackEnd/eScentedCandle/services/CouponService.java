package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

public interface CouponService {
    double calculateCouponValue(String couponCode, double totalAmount);
}
