package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.eScentedCandle_project_BackEnd.eScentedCandle.converters.UserConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponses {
    private int status;
    private String message;
    private ResponseData data;

    @Data
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Setter
    @Getter
    public static class ResponseData {
        public UserConverter user;
        @JsonProperty("token")
        public String token;

        @JsonProperty("refresh_token")
        private String refreshToken;
    }
}
