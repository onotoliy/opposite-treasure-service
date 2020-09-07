package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.dto.EventSearchParameter;
import com.github.onotoliy.opposite.treasure.services.IEventService;
import com.github.onotoliy.opposite.treasure.web.core.AbstractModifierResource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WEB сервис управления событиями.
 *
 * @author Anatoliy Pokhresnyi
 */
@RestController
@RequestMapping(value = "/event")
public class EventResource
extends AbstractModifierResource<
    Event,
    EventSearchParameter,
    IEventService> {

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    @Autowired
    public EventResource(final IEventService service) {
        super(service);
    }

    /**
     * Получение списка всех событий.
     *
     * @return События.
     */
    @GetMapping(value = "/list")
    public List<Option> getAll() {
        return service.getAll();
    }

    /**
     * Поиск событий.
     *
     * @param name Название.
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return События.
     */
    @GetMapping
    public Page<Event> getAll(
            @RequestParam(value = "name", required = false)
            final String name,
            @RequestParam(value = "offset",
                          required = false,
                          defaultValue = "0")
            final int offset,
            @RequestParam(value = "numberOfRows",
                          required = false,
                          defaultValue = "10")
            final int numberOfRows) {
        return service.getAll(new EventSearchParameter(name,
                                                       offset,
                                                       numberOfRows));
    }
}
