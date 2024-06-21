package com.eScentedCandle_project_BackEnd.eScentedCandle.models;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.OrderStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentStatusEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_Date")
    private LocalDate orderDate;

    @Column(name = "total_Amount")
    private double totalAmount;

    @Column(name = "sub_total")
    private double subTotal;

    @Column(name = "payment_method", length = 255)
    private String paymentMethod;

    @Column(name = "province", length = 255)
    private String province;

    @Column(name = "district", length = 255)
    private String district;

    @Column(name = "ward", length = 255)
    private String ward;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "shipping_method", length = 255)
    private String shippingMethod;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "notes", length = 255)
    private String notes;

    @Column(name = "discounts", length = 255)
    private String discounts;

    @Column(name = "fullname", length = 255)
    private String fullName;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone_number", length = 255)
    private String phoneNumber;

    @Column(name = "active")
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    //@JsonBackReference
    private Coupon coupon;

    @Column(name = "time_order", length = 255)
    private LocalTime timeOrder;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentStatus;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    private User user;

    @Override
    protected void setDefaultTimeOrder() {
        if (this.timeOrder == null) {
            this.timeOrder = LocalTime.now();
        }
    }
}


