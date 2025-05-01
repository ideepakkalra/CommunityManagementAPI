package com.github.ideepakkalra.eventmanagement.exceptions;

public class InvalidCredentialsException extends EventManagementException {
    public InvalidCredentialsException() {
        super("Invalid phone number / password.");
    }
}
