package com.github.ideepakkalra.eventmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    public enum Status {
        SUCCESS, FAILURE
    }
    private Status status;
    private String message;
}
