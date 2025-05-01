package com.github.ideepakkalra.eventmanagement.model;

import lombok.Data;

@Data
public class LoginResponse {
    public enum Status {
        SUCCESS, FAILURE, LOCKED
    }
    private Status status;
    private String message;
}
