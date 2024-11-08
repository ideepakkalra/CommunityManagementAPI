package com.github.ideepakkalra.eventmanagement.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
}