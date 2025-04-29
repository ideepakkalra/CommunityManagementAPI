package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "T_USER")
public class User {
    public enum Gender {
        MALE, FEMALE
    }
    public enum Status {
        REFERRED, SUBMITTED, ACCEPTED
    }
    public enum Type {
        STANDARD, ADMIN
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Version
    private Integer version;
    @Column(unique = true)
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String description;
    private Gender gender;
    private Date dateOfBirth;
    private List<String> hobbies;
    private String photo;
    private List<String> hereFor;
    @OneToOne
    private User referredBy;
    @OneToOne
    private User updatedBy;
    private Date updatedOn;
    private Status status = Status.REFERRED;
    private Type type = Type.STANDARD;
}