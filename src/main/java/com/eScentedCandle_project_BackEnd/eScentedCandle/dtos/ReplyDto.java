package com.eScentedCandle_project_BackEnd.eScentedCandle.dtos;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDto {
    private Long id;

    private String userFullName;

    private String comment;

    private int level;

    private Long userId;
    private Long feedbackId;

    //private Long parentId;
}
