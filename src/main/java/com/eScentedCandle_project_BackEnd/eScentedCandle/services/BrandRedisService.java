package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Brand;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface BrandRedisService {
    //Clear cached data in Redis
    void clear();
    List<Brand> getAllBrands() throws JsonProcessingException;
    void saveBrand() throws JsonProcessingException;
}
