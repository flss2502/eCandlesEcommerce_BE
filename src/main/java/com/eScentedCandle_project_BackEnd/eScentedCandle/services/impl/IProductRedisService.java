package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ProductResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.ProductRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//@RequiredArgsConstructor
public class IProductRedisService implements ProductRedisService {
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final ObjectMapper redisObjectMapper;
//
//    private String getKeyFrom(String keyword, PageRequest pageRequest) {
//        int pageNumber = pageRequest.getPageNumber();
//        int pageSize = pageRequest.getPageSize();
//        Sort sort = pageRequest.getSort();
//        String sortDirection = sort.getOrderFor("id")
//                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";
//        String key = String.format("all_product:%s:%s:%s:%s", keyword, pageNumber, pageSize, sortDirection);
//        return key;
//    }
//
//    @Override
//    public void clear() {
//        redisTemplate.getConnectionFactory().getConnection().flushAll();
//    }
//
//    @Override
//    public List<ProductResponse> getAllProducts(String keyword, PageRequest pageRequest) throws JsonProcessingException {
//        String key = this.getKeyFrom(keyword, pageRequest);
//        String json = (String) redisTemplate.opsForValue().get(key);
//        List<ProductResponse> productResponses
//                = json != null ? redisObjectMapper.readValue(json,
//                new TypeReference<List<ProductResponse>>() {
//                }) : null;
//        return productResponses;
//    }
//
//    @Override
//    public void saveProduct(List<ProductResponse> productResponses, String keyword, PageRequest pageRequest) throws JsonProcessingException {
//        String key = this.getKeyFrom(keyword, pageRequest);
//        String json = redisObjectMapper.writeValueAsString(productResponses);
//        redisTemplate.opsForValue().set(key, json);
//    }

}
