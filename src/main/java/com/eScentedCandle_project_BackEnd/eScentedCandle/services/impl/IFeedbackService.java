package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.FeedbackDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Feedback;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Order;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Product;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.User;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.OrderStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.FeedbackRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.OrderRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.ProductRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.UserRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IFeedbackService implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Feedback createFeedback(FeedbackDto feedbackDto) throws DataNotFoundException {
        User user = userRepository.findById(feedbackDto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        Product product = productRepository.findById(feedbackDto.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        List<Order> orders = orderRepository.findByUserAndProduct(user.getId(), product.getId());
        if (orders.isEmpty()) {
            throw new DataNotFoundException("No orders found for this user and product");
        }
        boolean hasDeliveredOrder = orders.stream()
                .anyMatch(order -> order.getOrderStatus() == OrderStatusEnum.SHIPPING);

        if (!hasDeliveredOrder) {
            throw new DataNotFoundException("User must have a delivered order to leave feedback");
        }

//        User userName = userRepository.findByFullName()
        modelMapper.typeMap(FeedbackDto.class, Feedback.class)
                .addMappings(mappers -> mappers.skip(Feedback::setId));
        Feedback feedback = new Feedback();
        modelMapper.map(feedbackDto, feedback);
        feedback.setUser(user);
        feedback.setProduct(product);
        return feedbackRepository.save(feedback);
    }

    @Override
    public Page<Feedback> getAllFeedback(PageRequest pageRequest) {
        return feedbackRepository.findAll(pageRequest);
    }
}
