package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String wardName;
    private String districtName;
    private String provinceName;
    private String wardCode;
    private String districtCode;
    private String provinceCode;
    private String status;
    private String phoneNumber;
    private Boolean defaultAddress;
    private Long userAddressId;

}
