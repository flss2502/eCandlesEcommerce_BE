package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListObjectResponse
{
    private int status;
    private String message;
    private DataObjectResponse data;

    @Data
    @Builder
    public static class DataObjectResponse {
        List<UserResponseDTO> list;
    }
}
