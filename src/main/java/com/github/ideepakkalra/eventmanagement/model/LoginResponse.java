package com.github.ideepakkalra.eventmanagement.model;

public class LoginResponse {
    public enum Status {
        SUCCESS, FAILURE, LOCKED
    }
    private Status status;
    private String message;
}
