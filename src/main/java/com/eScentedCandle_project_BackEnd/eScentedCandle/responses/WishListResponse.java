package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListResponse {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("product_id")
    private Long productId;
}
