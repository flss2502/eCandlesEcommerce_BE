package com.eScentedCandle_project_BackEnd.eScentedCandle.controllers;

import com.eScentedCandle_project_BackEnd.eScentedCandle.dtos.AddressDto;
import com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions.DataNotFoundException;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Brand;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.AddressResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.OrderResponse;
import com.eScentedCandle_project_BackEnd.eScentedCandle.responses.ResponseObject;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.AddressService;
import com.eScentedCandle_project_BackEnd.eScentedCandle.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/address")
@CrossOrigin
public class AddressController {
    private final AddressService addressService;

    @CrossOrigin
    @PostMapping("")
    public ResponseEntity<?> createAddress(
            @Valid @RequestBody AddressDto addressDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.ok().body(errorMessages);
            }
            AddressResponse address = addressService.createAddress(addressDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(address)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(
            @PathVariable Integer id,
            @Valid @RequestBody AddressDto addressDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.ok().body(errorMessages);
            }
            AddressResponse address = addressService.updateAddress(id, addressDto);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(address)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAddressDefault(
            @PathVariable Integer id,
            @RequestParam Boolean defaultAddress
            //BindingResult result
    ) {
        try {
            AddressResponse address = addressService.updateDefaultAddress(id, defaultAddress);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(address)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error")
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/address_by_user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseObject<Object>> getAddressByUser(
            //@PathVariable Long userId
    ) throws DataNotFoundException {
        List<AddressResponse> address = addressService.getAddressByUserId();
        try {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(address)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error product " + e.getMessage())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseObject<Object>> getAddressById(
            @PathVariable Integer id
    ) throws DataNotFoundException {
        AddressResponse address = addressService.getAddressById(id);
        try {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(address)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error product " + e.getMessage())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ResponseObject<Object>> deleteBrand(
            @PathVariable Integer id
    ) throws DataNotFoundException {
        try {
            AddressResponse addressResponse = addressService.deleteAddress(id);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully")
                            .data(addressResponse)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Error " + e.getMessage())
                            .error(e.getMessage())
                            .build()
            );
        }
    }
}
