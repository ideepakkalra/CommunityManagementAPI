package com.github.ideepakkalra.eventmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class UserResponse extends BaseResponse {
    private Long id;
    private Integer version;
    private String phoneNumber;
    private String email;
    private String firstName;
    private String lastName;
    private String description;
    private String gender;
    private Date dateOfBirth;
    private Long referredBy;
    private Long updatedBy;
    private Date updatedOn;
    private String type;
}
