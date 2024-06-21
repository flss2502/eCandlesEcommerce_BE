package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class AuthenticationDto {
    private String email;
    private String password;
}
