package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.services.DebtService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WEB сервис чтения долгов.
 *
 * @author Anatoliy Pokhresnyi
 */
@RestController
@RequestMapping(value = "/debt")
public class DebtResource {

    /**
     * Сервис.
     */
    private final DebtService service;

    /**
     * Конструктор.
     *
     * @param service Сервис.
     */
    @Autowired
    public DebtResource(final DebtService service) {
        this.service = service;
    }

    /**
     * Получение списка событий по которым пользователь не рассчитался.
     *
     * @param person Пользователь
     * @return Список событий по которым пользователь не рассчитался.
     */
    @GetMapping("/person/{person}")
    public Page<Event> getDebts(@PathVariable("person") final UUID person) {
        return service.getDebts(person);
    }
}
