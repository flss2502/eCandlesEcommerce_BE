package com.eScentedCandle_project_BackEnd.eScentedCandle.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Brands")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@EntityListeners()
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String image;
}
