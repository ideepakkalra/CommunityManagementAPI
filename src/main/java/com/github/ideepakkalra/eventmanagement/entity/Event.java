package com.github.ideepakkalra.eventmanagement.entity;

import lombok.*;

import java.util.Date;

@Data
public class Event {
    private String name;
    private String description;
    private User createdBy;
    private Date createdOn;
    private Date start;
    private Date end;
    private Event parent;
}
