package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String status;
    @JsonProperty("token")
    private String token;
    @JsonProperty("refresh-token")
    private String refreshToken;
}
