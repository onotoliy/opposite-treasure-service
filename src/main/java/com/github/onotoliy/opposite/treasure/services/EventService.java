package com.github.onotoliy.opposite.treasure.services;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.repositories.EventRepository;
import com.github.onotoliy.opposite.treasure.services.core.AbstractModifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService
extends AbstractModifierService<Event, EventSearchParameter, EventRepository> {

    @Autowired
    public EventService(EventRepository repository) {
        super(repository);
    }

    public Page<Event> getDebts(UUID person) {
        return repository.getDebts(person);
    }
}
