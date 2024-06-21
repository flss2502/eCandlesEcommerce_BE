package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordResponse {
    private int status;
    private String message;
    private Forgot forgot;

    @Builder
    @Data
    public static class Forgot {
        private String note;
    }
}
