package com.eScentedCandle_project_BackEnd.eScentedCandle.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url", length = 255)
    private String imageUrl;

    //    @ManyToOne(cascade = {CascadeType.ALL})
//    @JoinColumn(name = "product_id", nullable = false)
//    @JsonIgnore
//    @JsonBackReference
//    private Product product;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
}
