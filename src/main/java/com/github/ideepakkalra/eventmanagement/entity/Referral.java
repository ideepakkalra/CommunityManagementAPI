package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "T_REFERRAL")
public class Referral {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String key;
    @Column(unique = true)
    private String phoneNumber;
}
