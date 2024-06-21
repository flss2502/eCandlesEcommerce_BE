package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListUserResponse {
    private int status;
    private String message;
    private DataResponse data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DataResponse {
        private UserResponseDTO userResponseDTO;
    }
}
