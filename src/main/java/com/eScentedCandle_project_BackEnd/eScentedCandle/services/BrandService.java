package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.BrandDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.CustomerResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Brand;

import java.time.LocalDate;
import java.util.List;

public interface BrandService {
    void createbrand(BrandDto brandDto);

    void updateBrand(Long id, BrandDto brandDto) throws DataNotFoundException;

    void removeBrand(Long id) throws DataNotFoundException;

    List<Brand> getAllBrand();

    CustomerResponse getCountTotalUser(LocalDate startDate, LocalDate endDate);
}
