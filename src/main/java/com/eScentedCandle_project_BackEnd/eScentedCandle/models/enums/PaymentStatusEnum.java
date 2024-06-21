package com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaymentStatusEnum {
    PENDING,
    CANCELLED,
    CONFIRMED
}
