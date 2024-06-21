package com.eScentedCandle_project_BackEnd.eScentedCandle.responses.analysisDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalOrderReponse {
    private Long TotalOrder;
}
