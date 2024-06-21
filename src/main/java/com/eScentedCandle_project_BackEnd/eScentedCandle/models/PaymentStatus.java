package com.eScentedCandle_project_BackEnd.eScentedCandle.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStatus extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "code", length = 255)
    private String code;
}
