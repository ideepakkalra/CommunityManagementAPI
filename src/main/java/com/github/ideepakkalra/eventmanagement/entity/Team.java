package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "T_TEAM")
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Version
    private Integer version;
    private String name;
    private String description;
    private String leader;
    @OneToMany
    private Set<TeamMember> members;
    @ManyToOne
    private Event event;
}
