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
    EventService> {

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    @Autowired
    public EventResource(final EventService service) {
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

    /**
     * Получение списка событий по которым пользователь не рассчитался.
     *
     * @param person Пользователь
     * @return Список событий по которым пользователь не рассчитался.
     */
    @GetMapping("/person/{person}/debts")
    public Page<Event> getDebts(@PathVariable("person") final UUID person) {
        return service.getDebts(person);
    }
}
