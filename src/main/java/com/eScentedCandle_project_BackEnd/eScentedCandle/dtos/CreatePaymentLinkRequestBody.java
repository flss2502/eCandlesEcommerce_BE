package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentMethodEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class CreatePaymentLinkRequestBody {
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

  private String returnUrl;
  private String cancelUrl;

}