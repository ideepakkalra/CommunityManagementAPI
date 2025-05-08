package com.github.ideepakkalra.eventmanagement.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class UserRequest {
    private Long id;
    private Integer version = 0;
    @NotEmpty(message = "Invalid phone number.")
    @Size(min = 10, max = 14, message = "Invalid phone number. Must be between 10-14 numbers with +country code.")
    @Pattern(regexp = "^\\+\\d{1,3}\\d{4,14}$", message = "Invalid phone number. Only numbers allowed.")
    private String phoneNumber;
    @Email (message = "Invalid email address.")
    //@Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email address.")
    private String email;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid first name.")
    @Size (max = 100, message = "Invalid first name.")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid last name.")
    @Size (max = 100, message = "Invalid last name.")
    private String lastName;
    @Size (max = 1000, message = "Invalid description.")
    private String description;
    @Pattern(regexp = "MALE|FEMALE", message = "Invalid gender.")
    private String gender;
    @Past (message = "Invalid date of birth.")
    private Date dateOfBirth;
    @NotNull (message = "Invalid referred by.")
    @PositiveOrZero (message = "Invalid referred by.")
    private Integer referredBy;
    @Null (message = "Invalid updated by.")
    private Integer updatedBy;
    @Null (message = "Invalid updated on.")
    private Date updatedOn;
    @Pattern(regexp = "REFERRED|SUBMITTED|ACCEPTED", message = "Invalid gender.")
    private String status;
    @Pattern(regexp = "STANDARD|ADMIN", message = "Invalid type.")
    private String type;
    @Size (min = 6, max = 6, message = "Invalid passcode size. Must be 6 numbers in size.")
    @Pattern(regexp = "^\\d{6}$", message = "Invalid passcode. Only numbers allowed.")
    private String passcode;
}
