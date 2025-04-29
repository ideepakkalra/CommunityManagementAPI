package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "T_REFERRAL")
public class Referral {
    public enum State {
        OPEN, CLOSED
    }
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String key;
    @Version
    private Integer version;
    @ManyToOne
    private User referrer;
    @Column(unique = true)
    private String phoneNumber;
    private State state = State.OPEN;
    @OneToOne
    private User updatedBy;
    private Date updatedOn;
}
