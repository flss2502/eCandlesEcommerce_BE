package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPassWordResponse {
    private int status;
    private String message;
}
