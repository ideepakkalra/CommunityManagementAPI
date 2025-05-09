package com.github.ideepakkalra.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "T_EVENT")
public class Event {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Version
    private int version;
    private String name;
    private String description;
    private Date startAt;
    private Date endAt;
    private Date updatedOn;
    @OneToOne
    private User updatedBy;
}
