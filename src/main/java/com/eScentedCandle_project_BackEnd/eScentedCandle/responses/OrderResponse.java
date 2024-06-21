package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CartItemDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.OrderStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse extends BaseResponse {
    private Long id;
    //private LocalDateTime orderDate;
    private OrderStatusEnum orderStatus;
    private PaymentStatusEnum paymentStatus;
    private double totalAmount;
    private double subTotal;
    private String paymentMethod;
    //private String shippingMethod;
    //private LocalDate shippingDate;
    private String notes;
    //private String discounts;
    private String fullName;
    //private String email;
    private String phoneNumber;
    private String province;
    private String district;
    private String ward;
    private String address;
    private Boolean active;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime timeOrder;
    private List<CartItemResponse> cartItems;
    //private int orderDetails;
    private Long userResponse;
    //private Long couponId;
}
