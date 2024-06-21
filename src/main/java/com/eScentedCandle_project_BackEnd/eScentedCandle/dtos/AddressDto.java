package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    //private Long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("street_address")
    @NotBlank(message = "Street address is required")
    private String streetAddress;
    @JsonProperty("ward_code")
    private String wardCode;
    @JsonProperty("district_code")
    private String districtCode;
    @JsonProperty("province_code")
    private String provinceCode;
    @JsonProperty("ward_name")
    private String wardName;
    @JsonProperty("district_name")
    private String districtName;
    @JsonProperty("province_name")
    private String provinceName;
    private String status;
    @NotBlank(message = "Phone number is required")
    @JsonProperty("phone_number")
    private String phoneNumber;
//    @JsonProperty("token_user")
//    private String tokenUser;
}
