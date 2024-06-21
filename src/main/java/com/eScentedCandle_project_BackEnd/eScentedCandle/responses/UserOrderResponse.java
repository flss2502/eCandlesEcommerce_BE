package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOrderResponse {
    private Long id;
    private String email;
    private Date dateOfBirth;
    private String address;
    private String fullName;
    private String phoneNumber;
}
