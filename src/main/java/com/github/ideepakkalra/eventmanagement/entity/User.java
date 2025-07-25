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
@Table(name = "T_USER")
public class User {
    public enum Gender {
        MALE, FEMALE
    }
    public enum State {
        PENDING_APPROVAL, INACTIVE, ACTIVE
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
    @ToString.Exclude
    @ManyToOne
    private User referredBy;
    @ToString.Exclude
    @ManyToOne
    private User updatedBy;
    private Date updatedOn;
    private State state = State.PENDING_APPROVAL;
    private Type type = Type.STANDARD;
}