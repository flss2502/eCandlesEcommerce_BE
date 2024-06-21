package com.eScentedCandle_project_BackEnd.eScentedCandle.converters;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CartItemDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.OrderDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Order;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.CartItemResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {
    public static OrderResponse toResponse(Order order) {
        List<CartItemResponse> cartItemResponses = order.getOrderDetails().stream()
                .map(cartItem -> CartItemResponse.builder()
                        .id(cartItem.getId())
                        .productId(cartItem.getProduct().getId())
                        .quantity(cartItem.getQuantity())
                        .productResponse(ProductConverter.toResponse(cartItem.getProduct()))
                        .build())
                .collect(Collectors.toList());

        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(order.getTotalAmount())
                .subTotal(order.getSubTotal())
                .paymentMethod(order.getPaymentMethod())
                //.shippingMethod(order.getShippingMethod())
                //.shippingDate(order.getShippingDate())
                .notes(order.getNotes())
                //.discounts(order.getDiscounts())
                .fullName(order.getFullName())
                //.email(order.getEmail())
                .timeOrder(order.getTimeOrder())
                .phoneNumber(order.getPhoneNumber())
                .province(order.getProvince())
                .district(order.getDistrict())
                .ward(order.getWard())
                .address(order.getAddress())
                .active(order.getActive())
                .cartItems(cartItemResponses)
                .userResponse(order.getUser().getId())
                //.couponId(order.getCoupon().getId())
                .build();
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setUpdatedAt(order.getUpdatedAt());
        return orderResponse;
    }

    public static Order toEntity(OrderDto orderDto) {
        Order entity = new Order();
        entity.setTotalAmount(orderDto.getTotalAmount());
        entity.setSubTotal(orderDto.getSubTotal());
        //entity.setShippingMethod(orderDto.getShippingMethod());
        entity.setNotes(orderDto.getNotes());
        //entity.setDiscounts(orderDto.getDiscounts());
        entity.setFullName(orderDto.getFullName());
        //entity.setEmail(orderDto.getEmail());
        entity.setPhoneNumber(orderDto.getPhoneNumber());
        entity.setProvince(orderDto.getProvince());
        entity.setWard(orderDto.getWard());
        entity.setDistrict(orderDto.getDistrict());
        entity.setPaymentMethod(String.valueOf(orderDto.getPaymentMethod()));
        entity.setAddress(orderDto.getAddress());
        return entity;
    }
}
