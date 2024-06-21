package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePassWordDTO {

    private String currentPassWord;
    private String newPassWord;
    private String confirmPasWord;
}
