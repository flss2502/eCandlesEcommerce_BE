package com.eScentedCandle_project_BackEnd.eScentedCandle.exceptions;

public class InsufficientQuantityException extends RuntimeException{
    public InsufficientQuantityException(String message) {
        super(message);
    }
}
