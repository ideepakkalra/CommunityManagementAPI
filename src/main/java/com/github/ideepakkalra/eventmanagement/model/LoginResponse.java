package com.github.ideepakkalra.eventmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    public enum Status {
        SUCCESS, FAILURE, LOCKED
    }
    private Status status;
    private String message;
}
