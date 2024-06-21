package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.ProductInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInformationRepository extends JpaRepository<ProductInformation, Long> {
}
