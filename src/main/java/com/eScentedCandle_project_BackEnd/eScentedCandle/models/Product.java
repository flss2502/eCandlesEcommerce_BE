package com.eScentedCandle_project_BackEnd.eScentedCandle.models;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.listeners.ProductListener;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(ProductListener.class)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name", length = 255)
    private String productName;
    private String thumbnail;
    private Float cost;
    private Float price;
    private Float discount;
    @Column(name = "product_code", length = 255)
    private String productCode;
    private String availability;
    private int quantity;
    private Boolean status;
    //private String type;
    @Column(name = "product_dimensions")
    private String productDimensions;
    @Column(name = "product_detail", columnDefinition = "TEXT")
    private String productDetail;
//    @ManyToOne
//    @JoinColumn(name = "brand_id", nullable = false)
//    private Brand brand;
//    @ManyToOne
//    @JoinColumn(name = "type_id", nullable = false)
//    private CategoryType categoryType;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_category_type",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_type_id")
    )
    private List<CategoryType> categoryTypes = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductImages> productImages;
}
