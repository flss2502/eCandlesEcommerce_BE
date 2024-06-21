package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
}
