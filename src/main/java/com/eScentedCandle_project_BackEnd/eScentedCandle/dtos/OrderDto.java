package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CartItemDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentMethodEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    @JsonProperty("total_amount")
    @Min(value = 1, message = "Total money must be >= 0")
    private double totalAmount;

    @Min(value = 1, message = "Sub Total money must be >= 0")
    @JsonProperty("sub_total")
    private double subTotal;

    @JsonProperty("payment_method")
    private PaymentMethodEnum paymentMethod;

    private String notes;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 5, message = "Phone number must be at least 5 characters")
    private String phoneNumber;

    private String province;
    private String district;
    private String ward;
    private String address;

    @JsonProperty("cart_items")
    private List<CartItemDto> cartItems;

    private String returnUrl;
    private String cancelUrl;

    // Phương thức tính tổng số tiền dựa trên subTotal
    public double calculateSubTotal() {
        double subTotal = 0;
        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItemDto item : cartItems) {
                subTotal += item.getPrice() * item.getQuantity();
            }
        }
        return subTotal;
    }

    // Phương thức cập nhật lại totalAmount bằng tổng tiền mới tính
    public double calculateTotalAmount() {
        return subTotal; // Trả về subTotal làm totalAmount
    }

    // Phương thức kiểm tra tính hợp lệ của totalAmount so với subTotal
    public boolean isTotalAmountValid() {
        return totalAmount >= subTotal;
    }
}
