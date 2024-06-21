package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseListObject<T> {
    private T data;
    private int totalPages;
    private Long totalProducts;
}
