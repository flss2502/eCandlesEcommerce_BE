package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.OrderDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Order;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.OrderStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentMethodEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.OrderResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseListObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.OrderService;
import com.lib.payos.PayOS;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;
    private final PayOS payOS;

    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDto orderDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(orderDto.getPaymentMethod() == PaymentMethodEnum.PAYMENT_QR){
                var order1 = orderService.createOrderQR(orderDto);
                return ResponseEntity.ok(
                        ResponseObject.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Successfully")
                                .data(order1)
                                .build()
                );
            }
            OrderResponse order = orderService.createOrder(orderDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(order)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }





    @GetMapping("/get_all_order")
    public ResponseEntity<ResponseObject<Object>> getAllOrder(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) throws DataNotFoundException {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<OrderResponse> orderPage = orderService.getAllOrders(pageRequest);
        int totalPages = orderPage.getTotalPages();
        Long totalOrder = orderPage.getTotalElements();
        List<OrderResponse> orderResponseList = orderPage.getContent();
        ResponseListObject<List<OrderResponse>> responseListObject = ResponseListObject.<List<OrderResponse>>builder()
                .data(orderResponseList)
                .totalProducts(totalOrder)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully")
                        .data(responseListObject)
                        .build()
        );
    }

    @GetMapping("/get_order_by_payment_and_order_status")
    public ResponseEntity<ResponseObject<Object>> getOrderByPaymentAndOrderStatus(
            @RequestParam OrderStatusEnum orderStatus,
            @RequestParam PaymentStatusEnum paymentStatusEnum,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<OrderResponse> orderResponses = orderService.getOrdersByPaymentStatusAndOrderStatus(
                paymentStatusEnum, orderStatus, pageRequest
        );
        int totalPages = orderResponses.getTotalPages();
        Long totalOrder = orderResponses.getTotalElements();
        List<OrderResponse> orderResponseList = orderResponses.getContent();
        ResponseListObject<List<OrderResponse>> responseListObject = ResponseListObject.<List<OrderResponse>>builder()
                .data(orderResponseList)
                .totalProducts(totalOrder)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully")
                        .data(responseListObject)
                        .build()
        );
    }

    @GetMapping("/order_by_user_id")
    public ResponseEntity<ResponseObject<Object>> getOrderById(
            //@PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) throws DataNotFoundException {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<OrderResponse> orderResponses = orderService.getProductByUserId(pageRequest);
        int totalPages = orderResponses.getTotalPages();
        Long totalOrder = orderResponses.getTotalElements();
        List<OrderResponse> orderResponseList = orderResponses.getContent();
        ResponseListObject<List<OrderResponse>> responseListObject = ResponseListObject.<List<OrderResponse>>builder()
                .data(orderResponseList)
                .totalProducts(totalOrder)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully")
                        .data(responseListObject)
                        .build()
        );
        //return getResponseObjectResponseEntity(orderResponses);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<ResponseObject<Object>> updateOrder(
            @PathVariable Long orderId,
            @RequestParam OrderStatusEnum orderStatus,
            @RequestParam PaymentStatusEnum paymentStatus
    ) throws DataNotFoundException {
        try {
            OrderResponse updatedOrder = orderService.updateOrder(orderId, orderStatus, paymentStatus);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Order updated successfully")
                            .data(updatedOrder)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error updating order")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Object>> getOrderById(@PathVariable Long id) throws DataNotFoundException {
        try {
            OrderResponse updatedOrder = orderService.getOrderById(id);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(updatedOrder)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    private ResponseEntity<ResponseObject<Object>> getResponseObjectResponseEntity(Page<Order> orderPage) {
        int totalPages = orderPage.getTotalPages();
        Long totalOrder = orderPage.getTotalElements();
        List<Order> orderList = orderPage.getContent();
        ResponseListObject<List<Order>> responseListObject = ResponseListObject.<List<Order>>builder()
                .data(orderList)
                .totalProducts(totalOrder)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully")
                        .data(responseListObject)
                        .build()
        );
    }
}
