package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "T_LOGIN_LOG")
public class LoginLog {
    public enum Status {
        SUCCESS, FAILURE
    }
    private String countryCode;
    private String phoneNumber;
    private Status status;
    private Date date;
    private String ipAddress;
    private String metaData;
}
