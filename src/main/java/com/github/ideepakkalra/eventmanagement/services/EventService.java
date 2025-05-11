package com.github.ideepakkalra.eventmanagement.services;

import com.github.ideepakkalra.eventmanagement.db.EventRepository;
import com.github.ideepakkalra.eventmanagement.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public Event create(Event event) {
        return eventRepository.save(event);
    }
}
