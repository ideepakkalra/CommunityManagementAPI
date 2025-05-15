package com.github.ideepakkalra.eventmanagement.exceptions;

public class UserNotFoundException extends EventManagementException {
    public UserNotFoundException() {
        super("User not found.");
    }
}
