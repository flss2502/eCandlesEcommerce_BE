package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.common.WebCommon;
import com.eScentedCandle_project_BackEnd.eScentedCandle.components.LocalizationUtils;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.BrandDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto.CustomerResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Brand;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.BrandRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.UserRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.BrandService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IBrandService implements BrandService {
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final LocalizationUtils localizationUtils;

    @Override
    public void createbrand(BrandDto brandDto) {
        String generatedCode = WebCommon.generateCodeFromName(brandDto.getName());
        Brand brand = Brand.builder()
                .code(generatedCode)
                .name(brandDto.getName())
                .image(brandDto.getImage())
                .build();
        brandRepository.save(brand);
    }

    @Override
    public void updateBrand(Long id, BrandDto brandDto) throws DataNotFoundException {
        String generatedCode = WebCommon.generateCodeFromName(brandDto.getName());
        Brand existingBrand = brandRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.FOUND_ID_BRAND_FAILED)));
        existingBrand.setCode(generatedCode);
        existingBrand.setName(brandDto.getName());
        existingBrand.setImage(brandDto.getImage());
        brandRepository.save(existingBrand);
    }

    @Override
    public void removeBrand(Long id) throws DataNotFoundException {
        Brand existingBrand = brandRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException(localizationUtils
                        .getLocalizedMessage(MessageKeys.FOUND_ID_BRAND_FAILED)));
        brandRepository.delete(existingBrand);
    }

    @Override
    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    public CustomerResponse getCountTotalUser(LocalDate startDate, LocalDate endDate) {
        Long totalUser = userRepository.countAllByCreatedAtBetween(startDate, endDate);
        return new CustomerResponse(totalUser);
    }
}
