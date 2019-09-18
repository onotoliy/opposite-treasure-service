package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.services.EventService;
import com.github.onotoliy.opposite.treasure.web.core.AbstractModifierResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/event")
public class EventResource
extends AbstractModifierResource<Event, EventSearchParameter, EventService> {

    @Autowired
    public EventResource(EventService service) {
        super(service);
    }

    @GetMapping(value = "/list")
    public List<Option> getAll() {
        return service.getAll();
    }

    @GetMapping
    public Page<Event> getAll(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "numberOfRows", required = false, defaultValue = "10") int numberOfRows) {
        return service.getAll(new EventSearchParameter(name, offset, numberOfRows));
    }

    @GetMapping("/person/{person}/debts")
    public Page<Event> getDebts(@PathVariable("person") UUID person) {
        return service.getDebts(person);
    }
}
