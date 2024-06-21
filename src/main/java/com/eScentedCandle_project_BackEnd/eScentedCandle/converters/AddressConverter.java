package com.eScentedCandle_project_BackEnd.eScentedCandle.converters;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.AddressDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Address;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {
    public static AddressResponse toResponse(Address address) {
        AddressResponse addressResponse = AddressResponse.builder()
                .id(address.getId())
                .firstName(address.getFirstName())
                .lastName(address.getLastName())
                .streetAddress(address.getStreetAddress())
                .wardName(address.getWardName())
                .districtName(address.getDistrictName())
                .districtCode(address.getDistrictCode())
                .provinceCode(address.getProvinceCode())
                .wardCode(address.getWardCode())
                //.provinceCode(address.getProvinceName())
                .status(address.getStatus())
                .phoneNumber(address.getPhoneNumber())
                .defaultAddress(address.isDefault())
                .userAddressId(address.getUser().getId())
                .provinceName(address.getProvinceName())
                .build();
        return addressResponse;
    }

    public static Address toEntity (AddressDto dto){
        Address entity = new Address();
        entity.setFirstName(dto.getFirstName());
        //dto.setUserAddressId(entity.getUser().getId());
        entity.setLastName(dto.getLastName());
        entity.setStreetAddress(dto.getStreetAddress());
        entity.setWardCode(dto.getWardCode());
        entity.setWardName(dto.getWardName());
        entity.setDistrictCode(dto.getDistrictCode());
        entity.setDistrictName(dto.getDistrictName());
        entity.setProvinceCode(dto.getProvinceCode());
        entity.setProvinceName(dto.getProvinceName());
       // dto.setStatus(entity.getStatus());
        entity.setPhoneNumber(dto.getPhoneNumber());
        return entity;
    }
}
