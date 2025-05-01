package com.github.ideepakkalra.eventmanagement.exceptions;

public class AccountLockedException extends EventManagementException {
    public AccountLockedException() {
        super("Account is locked.");
    }
}
