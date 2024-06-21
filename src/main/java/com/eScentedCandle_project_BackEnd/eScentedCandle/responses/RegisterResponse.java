package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private int status;
    private String message;
    private DataRegister register;

    @Data
    @Builder
    public static class DataRegister {
        private UserResponseDTO userResponseDTO;

    }
}
