package com.eScentedCandle_project_BackEnd.eScentedCandle.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "information")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInformation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double weight;
    @Column(name = "ingredient_type", length = 255)
    private String ingredientType;
    private String brand;
    @Column(name = "from_product")
    private Integer fromProduct;
    @Column(name = "net_quantity", length = 255)
    private Double netQuantity;
    @Column(name = "product_dimension", length = 255)
    private String productDimension;
    private String asin;
    @Column(name = "generic_name", length = 255)
    private String genericName;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;
}
