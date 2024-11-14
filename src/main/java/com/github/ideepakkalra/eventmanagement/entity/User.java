package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "T_USER")
public class User {
    @Id
    private String email;
    @Version
    private Integer version;
    private String firstName;
    private String lastName;
    @ManyToMany
    private List<Team> teams;
}