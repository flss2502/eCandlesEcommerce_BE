package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.OrderConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.CartItemDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.OrderDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.InsufficientQuantityException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.*;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.OrderStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentMethodEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.*;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.OrderResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.PaymentSuccess;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.SumTotalOrderResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.TotalOrderReponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.JwtService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.OrderService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lib.payos.PayOS;
import com.lib.payos.type.ItemData;
import com.lib.payos.type.PaymentData;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IOrderService implements OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PayOS payOS;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final EmailUtil emailUtil;

    @Override
    public OrderResponse createOrder(OrderDto orderDto) throws DataNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization").substring(7);
        Long userId = extractUserIdFromToken(token);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
//        modelMapper.getConfiguration().setAmbiguityIgnored(true); //Eliminate ambiguous case
//        modelMapper.typeMap(OrderDto.class, Order.class)
//                .addMappings(mappers -> mappers.skip(Order::setId));
//        Order order = new Order();
//        modelMapper.map(orderDto, order);
        Order order = OrderConverter.toEntity(orderDto);
        order.setUser(existingUser);
        //order.setPaymentStatus(existingPaymentStatus);
        order.setOrderDate(LocalDate.now());
        //order.setOrderStatus(existingOrderStatus);
//        LocalDate shippingDate = orderDto.getShippingDate() == null
//                ? LocalDate.now() : orderDto.getShippingDate();
//        if (shippingDate.isBefore(LocalDate.now())) {
//            throw new DataNotFoundException("Date must be at least today !");
//        }
//        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setPaymentStatus(PaymentStatusEnum.PENDING);
        order.setOrderStatus(OrderStatusEnum.PENDING);
        order.setTotalAmount(orderDto.getTotalAmount());
        if (order.getOrderStatus() != null && order.getOrderStatus().toString() == OrderStatusEnum.SHIPPING.toString()) {
            updateProductQuantities(orderDto.getCartItems());
        }
        order.setActive(true);
        orderRepository.save(order);
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = null;
        for (CartItemDto cartItemDto : orderDto.getCartItems()
        ) {
            orderDetail = new OrderDetail();
            orderDetail.setOrders(order);
            Long productId = cartItemDto.getProductId();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found!!"));
            orderDetail.setProduct(product);
            orderDetail.setPrice(cartItemDto.getPrice());
            orderDetail.setQuantity(cartItemDto.getQuantity());
            //orderDetail.setDiscount(cartItemDto.get());
            orderDetail.setTotalAmount(cartItemDto.getTotalProduct());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);
        return OrderConverter.toResponse(order);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderStatusEnum orderStatus, PaymentStatusEnum paymentStatus) throws DataNotFoundException {
        Order orderExisting = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found!!"));
        orderExisting.setOrderStatus(orderStatus);
        orderExisting.setPaymentStatus(paymentStatus);
        orderRepository.save(orderExisting);
        String userEmail = orderExisting.getUser().getEmail();
        try {
            emailUtil.sendOrderStatusUpdate(orderExisting.getEmail(), orderExisting);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send order status update email, please try again");
        }
        return OrderConverter.toResponse(orderExisting);
    }

    //mapping ********
    @Override
    public Page<OrderResponse> getAllOrders(PageRequest pageRequest) throws DataNotFoundException {
        Page<Order> orders = orderRepository.findAll(pageRequest);
        //return orders.map(order -> modelMapper.map(order, OrderResponse.class));
        return orders.map(OrderConverter::toResponse);
    }

    @Override
    public Page<OrderResponse> getOrdersByPaymentStatusAndOrderStatus(
            PaymentStatusEnum paymentStatus,
            OrderStatusEnum orderStatus,
            PageRequest pageRequest) {
        Page<Order> orders = orderRepository.findAllByPaymentStatusOrOrderStatus(paymentStatus, orderStatus, pageRequest);
        return orders.map(OrderConverter::toResponse);
    }

    @Override
    public Page<OrderResponse> getProductByUserId(PageRequest pageRequest) throws DataNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization").substring(7);
        Long userId = extractUserIdFromToken(token);
        Page<Order> orders;
        orders = orderRepository.findByUserId(userId, pageRequest);
        return orders.map(OrderConverter::toResponse);
    }

    @Override
    public OrderResponse getOrderById(Long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found!!"));
        return OrderConverter.toResponse(order);
    }

    @Override
    public TotalOrderReponse getTotalOrder(LocalDate startDate, LocalDate endDate) {
        Long totalOrder = orderRepository.countOrdersByOrderDateBetween(startDate, endDate);
        return new TotalOrderReponse(totalOrder);
    }

    @Override
    public SumTotalOrderResponse getSumTotalOrder(LocalDate startDate, LocalDate endDate) {
        Double sumTotalOrder = orderRepository.sumTotalAmountByOrderDateBetween(startDate, endDate);
        return new SumTotalOrderResponse(sumTotalOrder);
    }

    @Override
    public ResponseEntity<ObjectNode> createOrderQR(OrderDto orderDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            // Validate the incoming order DTO
            if (orderDto == null) {
                response.put("message", "Request body is null");
                return ResponseEntity.badRequest().body(response);
            }

            // Extract user information from the token
            String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                    .getRequest().getHeader("Authorization").substring(7);
            Long userId = extractUserIdFromToken(token);
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            // Check payment method
            if (orderDto.getPaymentMethod() != PaymentMethodEnum.PAYMENT_QR) {
                response.put("message", "Unsupported payment method");
                return ResponseEntity.badRequest().body(response);
            }

            // Calculate subtotal
            double subTotal = orderDto.calculateSubTotal();

            // Update total amount based on subTotal
            orderDto.setTotalAmount(subTotal);

            // Create and save the order
            Order order = OrderConverter.toEntity(orderDto);
            order.setPaymentStatus(PaymentStatusEnum.PENDING);
            order.setOrderStatus(OrderStatusEnum.PENDING);
            order.setActive(true);
            order.setEmail(existingUser.getEmail());
            order.setUser(existingUser);
            Order savedOrder = orderRepository.save(order);

            // Create and save order details
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartItemDto item : orderDto.getCartItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new DataNotFoundException("Product not found for ID: " + item.getProductId()));
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrders(savedOrder);
                orderDetail.setProduct(product);
                orderDetail.setPrice(item.getPrice());
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setTotalAmount(item.getTotalProduct());
                orderDetails.add(orderDetail);
            }
            orderDetailRepository.saveAll(orderDetails);

            // Create item data list for payment processing
            List<ItemData> itemDataList = orderDto.getCartItems().stream().map(item -> {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found for ID: " + item.getProductId()));
                int itemPrice = (int) Math.round(item.getPrice());
                return new ItemData(product.getProductName(), item.getQuantity(), itemPrice);
            }).collect(Collectors.toList());

            String currentTimeString = String.valueOf(new Date().getTime());
            int orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

            PaymentData paymentData = new PaymentData(
                    orderCode,
                    (int) orderDto.getTotalAmount(),
                    orderDto.getNotes(),
                    itemDataList,
                    orderDto.getCancelUrl(),
                    orderDto.getReturnUrl()
            );
            // Create payment link
            JsonNode data;
            try {
                data = payOS.createPaymentLink(paymentData);

            } catch (JsonParseException e) {
                response.put("message", "Invalid JSON response received");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            } catch (Exception e) {
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            // Return success response
            response.put("message", "Order created successfully");
            response.put("orderId", savedOrder.getId());
            response.set("data", data);
            response.put("redirectUrl", "https://yourfrontendurl.com/order-confirmation");
            return ResponseEntity.ok(response);

        } catch (DataNotFoundException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error occurred while creating order");
            response.put("errorUrl", "https://yourfrontendurl.com/error-page");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    @Override
    public ResponseEntity<PaymentSuccess> updatePaymentStatus(Long orderID) {
        return orderRepository.findById(orderID)
                .map(order -> {
                    order.setPaymentStatus(PaymentStatusEnum.CONFIRMED);
                    orderRepository.save(order);
                    return ResponseEntity.ok(PaymentSuccess.builder()
                            .status(HttpStatus.OK.value())
                            .message("Payment successful")
                            .data(OrderConverter.toResponse(order))
                            .build());
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(PaymentSuccess.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message("Order not found")
                                .data(null)
                                .build()));
    }



    @Override
    public ResponseEntity<PaymentSuccess> cancelOrder(Long orderID) {
        return orderRepository.findById(orderID)
                .map(order -> {
                    order.setPaymentStatus(PaymentStatusEnum.CANCELLED);
                    orderRepository.save(order);
                    return ResponseEntity.ok(PaymentSuccess.builder()
                            .status(HttpStatus.OK.value())
                            .message("Payment status updated successfully")
                            .data(null) // Assuming you don't want to return data when canceling the order
                            .build());
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(PaymentSuccess.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message("Order not found")
                                .data(null)
                                .build()));
    }



    private void updateProductQuantities(List<CartItemDto> cartItems) throws DataNotFoundException {
        for (CartItemDto cartItemDto : cartItems) {
            Long productId = cartItemDto.getProductId();
            int quantity = cartItemDto.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));

            // Ensure that the quantity to be deducted is not greater than the available quantity
            if (quantity > product.getQuantity()) {
                throw new InsufficientQuantityException("Not enough quantity available for product with id: " + productId);
            }

            int updatedQuantity = product.getQuantity() - quantity;
            product.setQuantity(updatedQuantity);
            productRepository.save(product);
        }
    }

    public Long extractUserIdFromToken(String token) throws DataNotFoundException {
        String userEmail = jwtService.extractUserEmail(token); // Extract email from token
        User user = userRepository.findByEmail(userEmail) // Find user by email
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
        return user.getId();
    }
}
