package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT od.product, SUM(od.quantity) AS totalQuantitySold " +
            "FROM OrderDetail od " +
            "WHERE od.orders.orderDate = CURRENT_DATE " + // Get the current day's orders
            "GROUP BY od.product " +
            "ORDER BY totalQuantitySold DESC")
    List<Object[]> findTop5DailyBestSellingProducts();
}
