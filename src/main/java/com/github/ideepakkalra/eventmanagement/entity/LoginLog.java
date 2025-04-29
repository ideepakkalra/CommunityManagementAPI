package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "T_LOGIN_LOG")
public class LoginLog {
    public enum Status {
        SUCCESS, FAILURE
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String countryCode;
    private String phoneNumber;
    private Status status;
    private Date date;
    //private Map<String, String> metaData;
}
