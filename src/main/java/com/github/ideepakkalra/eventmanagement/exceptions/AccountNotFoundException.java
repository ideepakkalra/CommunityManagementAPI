package com.github.ideepakkalra.eventmanagement.exceptions;

public class AccountNotFoundException extends EventManagementException {
    public AccountNotFoundException() {
        super("Account not found.");
    }
}
