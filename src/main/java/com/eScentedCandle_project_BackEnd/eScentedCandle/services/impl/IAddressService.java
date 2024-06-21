package com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl;

import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.AddressConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.CategoryTypeConverter;
import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.AddressDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Address;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.User;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.AddressRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.repositories.UserRepository;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.AddressResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.AddressService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.JwtService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IAddressService implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    public AddressResponse createAddress(AddressDto addressDto) throws DataNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization").substring(7);
        Long userId = extractUserIdFromToken(token);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
//        modelMapper.getConfiguration().setAmbiguityIgnored(true);
//        modelMapper.typeMap(AddressDto.class, Address.class)
//                .addMappings(mapper -> mapper.skip(Address::setId));
//                //.addMappings(mapper -> mapper.skip(Address::setUser));
//
//        Address address = new Address();
//        modelMapper.map(addressDto, address);
        Address address = AddressConverter.toEntity(addressDto);
        address.setUser(existingUser);
        addressRepository.save(address);
        return AddressConverter.toResponse(address);
    }

    private Long extractUserIdFromToken(String token) throws DataNotFoundException {
        String userEmail = jwtService.extractUserEmail(token); // Extract email from token
        User user = userRepository.findByEmail(userEmail) // Find user by email
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
        return user.getId();
    }

    @Override
    public AddressResponse updateAddress(Integer id, AddressDto addressDto) throws DataNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization").substring(7);
        Long userId = extractUserIdFromToken(token);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Address not found!!!"));
        modelMapper.typeMap(AddressDto.class, Address.class)
                .addMappings(mapper -> mapper.skip(Address::setId));
        //Address address = new Address();
        modelMapper.map(addressDto, existingAddress);
        existingAddress.setUser(existingUser);
        addressRepository.save(existingAddress);
        return AddressConverter.toResponse(existingAddress);
    }

    @Override
    public AddressResponse updateDefaultAddress(Integer id, Boolean defaultAddress) throws DataNotFoundException {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Address not found!!!"));
        if (defaultAddress) {
            List<Address> userAddresses = addressRepository.findByUserId(existingAddress.getUser().getId());
            for (Address address : userAddresses) {
                if (!address.getId().equals(id)) {
                    address.setDefault(false);
                }
            }
            addressRepository.saveAll(userAddresses);
        }

        existingAddress.setDefault(defaultAddress);
        addressRepository.save(existingAddress);

        return AddressConverter.toResponse(existingAddress);
    }

    @Override
    public AddressResponse deleteAddress(Integer id) throws DataNotFoundException {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Address not found!!!"));
        addressRepository.delete(existingAddress);
        return AddressConverter.toResponse(existingAddress);
    }

    @Override
    public List<AddressResponse> getAddressByUserId() throws DataNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization").substring(7);
        Long userId = extractUserIdFromToken(token);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
        List<Address> addressList = addressRepository.findByUserId(existingUser.getId());
        return addressList.stream()
                .map(AddressConverter::toResponse).collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddressById(Integer id) throws DataNotFoundException {
        Address existingUser = addressRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found!!!"));
        return AddressConverter.toResponse(existingUser);
    }


}
