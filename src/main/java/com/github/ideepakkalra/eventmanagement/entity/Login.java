package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
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
    private Integer retryCount = 0;
    //private Map<String, String> metaData;
}
