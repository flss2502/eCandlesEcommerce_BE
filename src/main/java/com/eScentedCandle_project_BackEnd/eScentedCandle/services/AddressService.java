package com.eScentedCandle_project_BackEnd.eScentedCandle.services;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.AddressDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Address;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(AddressDto addressDto) throws DataNotFoundException;

    AddressResponse updateAddress(Integer id, AddressDto addressDto) throws DataNotFoundException;

    AddressResponse updateDefaultAddress(Integer id,Boolean defaulAddress) throws DataNotFoundException;

    AddressResponse deleteAddress(Integer id) throws DataNotFoundException;

    List<AddressResponse> getAddressByUserId() throws DataNotFoundException;

    AddressResponse getAddressById(Integer id) throws DataNotFoundException;
}
