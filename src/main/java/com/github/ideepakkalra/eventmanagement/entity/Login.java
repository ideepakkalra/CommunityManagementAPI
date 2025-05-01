package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "T_LOGIN")
public class Login {
    public enum Status {
        SUCCESS, FAILURE, LOCKED
    }
    @Id
    private String phoneNumber;
    private String passcode;
    private Status status;
    private Date loginDate;
    private Date logoutDate;
    @OneToOne
    private User user;
    private Integer retryCount;
    //private Map<String, String> metaData;
}
