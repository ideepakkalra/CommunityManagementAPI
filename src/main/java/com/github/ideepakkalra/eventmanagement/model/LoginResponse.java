package com.github.ideepakkalra.eventmanagement.model;

import com.github.ideepakkalra.eventmanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse extends BaseResponse {
    private Long userId;
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
