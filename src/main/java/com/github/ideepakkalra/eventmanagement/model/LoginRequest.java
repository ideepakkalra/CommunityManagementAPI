package com.github.ideepakkalra.eventmanagement.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "Invalid phone number.")
    @Size (min = 10, max = 14, message = "Invalid phone number. Must be between 10-14 numbers with +country code.")
    @Pattern(regexp = "^\\+\\d{1,3}\\d{4,14}$", message = "Invalid phone number. Only numbers allowed.")
    private String phoneNumber;
    @NotEmpty (message = "Invalid passcode.")
    @Size (min = 6, max = 6, message = "Invalid passcode size. Must be 6 numbers in size.")
    @Pattern(regexp = "^\\d{6}$", message = "Invalid passcode. Only numbers allowed.")
    private String passcode;
}
