package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "T_USER")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Version
    private int version;
    private String firstName;
    private String lastName;
    private String email;
    @ElementCollection
    private List<String> roles;
}