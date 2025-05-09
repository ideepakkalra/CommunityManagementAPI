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
@Table(name = "T_COMMUNITY_REFERRAL")
public class CommunityReferral {
    public enum State {
        OPEN, CLOSED
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String code;
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
