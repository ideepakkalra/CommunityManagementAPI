package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "T_USER")
public class User {
    public enum Gender {
        MALE, FEMALE
    }
    public enum Status {
        PENDING, SUBMITTED, ACCEPTED
    }
    public enum Type {
        STANDARD, ADMIN
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Version
    private Integer version;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String description;
    private String countryCode;
    private String phoneNumber;
    private Gender gender;
    private Date dateOfBirth;
    private List<String> hobbies;
    private Map<String, String> socialURLs;
    private String photo;
    private User referredBy;
    private User updatedBy;
    private Date updatedOn;
    private List<String> hereFor;
    private Status status;
    private Type type;
}