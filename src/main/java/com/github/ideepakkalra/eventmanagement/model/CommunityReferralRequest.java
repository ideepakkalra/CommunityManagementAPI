package com.github.ideepakkalra.eventmanagement.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommunityReferralRequest {
    @PositiveOrZero (message = "Invalid id.")
    private Long id;
    @Pattern( regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid code.")
    private String code;
    @PositiveOrZero (message = "Invalid id.")
    private Integer version;
    @PositiveOrZero (message = "Invalid referrer.")
    private Long referrer;
    @NotEmpty (message = "Invalid phone number.")
    @Size (min = 10, max = 14, message = "Invalid phone number. Must be between 10-14 numbers with +country code.")
    @Pattern (regexp = "^\\+\\d{1,3}\\d{4,14}$", message = "Invalid phone number. Only numbers allowed.")
    private String phoneNumber;
    @Pattern (regexp = "OPEN|CLOSED", message = "Invalid state.")
    private String state;
}
