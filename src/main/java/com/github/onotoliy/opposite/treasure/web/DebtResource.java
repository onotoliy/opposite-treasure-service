package com.github.onotoliy.opposite.treasure.web;

import com.github.onotoliy.opposite.data.Debt;
import com.github.onotoliy.opposite.data.Deposit;
import com.github.onotoliy.opposite.data.Event;
import com.github.onotoliy.opposite.data.Option;
import com.github.onotoliy.opposite.data.page.Page;
import com.github.onotoliy.opposite.treasure.services.DebtService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * Получение должников.
     *
     * @param event Событие.
     * @return Список должников.
     */
    @GetMapping("/event/{event}")
    public Page<Deposit> getDebtors(@PathVariable("event") final UUID event) {
        return service.getDebtors(event);
    }

    /**
     * Получение версии сущности.
     *
     * @return Версия сущности.
     */
    @GetMapping(value = "/version")
    public Option version() {
        return service.version();
    }

    /**
     * Данные, которые необходимо синхронизировать.
     *
     * @param offset Количество записей которое необходимо пропустить.
     * @param numberOfRows Размер страницы.
     * @return Данные, которые необходимо синхронизировать.
     */
    @GetMapping(value = "/sync")
    public Page<Debt> sync(
            @RequestParam(value = "offset",
                          required = false,
                          defaultValue = "0")
            final int offset,
            @RequestParam(value = "numberOfRows",
                          required = false,
                          defaultValue = "10")
            final int numberOfRows
    ) {
        return service.sync(offset, numberOfRows);
    }
}
